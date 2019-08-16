package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.danny.instacrawlerkotlin.model.Edge
import me.danny.instacrawlerkotlin.model.EdgeOwnerToTimelineMedia
import me.danny.instacrawlerkotlin.model.entity.GelatoInstaAccountBulk
import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.repository.GelatoInstaAccountBulkRepository
import me.danny.instacrawlerkotlin.repository.GelatoInstaAccountRepository
import me.danny.instacrawlerkotlin.repository.GelatoInstaMediaBulkRepository
import me.danny.instacrawlerkotlin.utils.getInstagramPostId
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.function.Tuple3
import reactor.util.function.Tuples
import java.sql.Timestamp

/**
 *
 * Created by danny.ban on 2019-08-14.
 *
 * @author danny.ban
 * @since
 */
@Service
class GelatoInstaAccountService {
    @Autowired
    private lateinit var gelatoInstaAccountBulkRepository: GelatoInstaAccountBulkRepository

    @Autowired
    private lateinit var gelatoInstaMediaBulkRepository : GelatoInstaMediaBulkRepository
    @Autowired
    lateinit var webClientBuilder: WebClient.Builder

    @Async
    fun startCrawling() {
        while (true) {
            val targetAccount = gelatoInstaAccountBulkRepository.findByIsCrawlingTrue() ?: break
            // READY
            targetAccount.result = "DOING"
            gelatoInstaAccountBulkRepository.save(targetAccount)

            try {
                // DOING
                getMediaFromInsta(targetAccount)
                targetAccount.isInstaCrawling = true
                targetAccount.result = "DONE"
                gelatoInstaAccountBulkRepository.save(targetAccount)
            } catch (e: Exception) {
                targetAccount.result = "FAILED"
                gelatoInstaAccountBulkRepository.save(targetAccount)
            }
        }
    }

    private fun getMediaFromInsta(targetAccount: GelatoInstaAccountBulk) {
        var edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia? = null

        do {
            edgeOwnerToTimelineMedia = getEdgeOwnerToTimelineMedia(targetAccount.accountId, edgeOwnerToTimelineMedia?.pageInfo?.endCursor)
            targetAccount.endCursor = edgeOwnerToTimelineMedia.pageInfo.endCursor
            gelatoInstaAccountBulkRepository.save(targetAccount)

            if (!saveInstaMedia(edgeOwnerToTimelineMedia.edges)) {
                edgeOwnerToTimelineMedia.pageInfo.hasNextPage = false
            }

            Thread.sleep(randomRange(5, 7) * 1000L)
        } while (edgeOwnerToTimelineMedia?.pageInfo?.hasNextPage ?: run { false })
    }

    private fun saveInstaMedia(edges: List<Edge>): Boolean {
        val limitDateTime = "2018-08-29 00:00:00.0" // 형식을 지켜야 함
        val limitDateTimeStamp = java.sql.Timestamp.valueOf(limitDateTime)

        val startDateTime = "2018-08-30 00:00:00.0" // 형식을 지켜야 함
        val endDateTime = "2018-11-02 00:00:00.0" // 형식을 지켜야 함
        val startDateTimeStamp = java.sql.Timestamp.valueOf(startDateTime)
        val endDateTimeStamp = java.sql.Timestamp.valueOf(endDateTime)

        for (edge in edges) {
            val currentTime = edge.node.takenAtTimestamp.time * 1000
            if (limitDateTimeStamp.time >= currentTime ) {
                return false
            }

            if(startDateTimeStamp.time < currentTime && endDateTimeStamp.time > currentTime) {
                val targetMedia = gelatoInstaMediaBulkRepository.findByContentId(edge.node.id)
                if(targetMedia != null) {
                    targetMedia.isCrawling = true
                    targetMedia.instaCrawlingDate = Timestamp(System.currentTimeMillis())
                    targetMedia.instaCreatedDate = Timestamp(currentTime)
                    targetMedia.instaLikeCount = edge.node.edgeMediaPreviewLike.count.toInt()
                    targetMedia.instaCommentCount = edge.node.edgeMediaToComment.count.toInt()
                    targetMedia.imageUrl = edge.node.displayUrl
                    targetMedia.instaMediaId = edge.node.id

                    gelatoInstaMediaBulkRepository.save(targetMedia)
                }
            }
        }

        return true
    }

    private fun getEdgeOwnerToTimelineMedia(accountId: String, endCursor: String? = null): EdgeOwnerToTimelineMedia {
        val objectMapper = jacksonObjectMapper()
        var url = "https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=$accountId&first=50"
        if (endCursor != null) {
            url = "$url&after=$endCursor"
        }

        val doc = webClientBuilder.build().get().uri(url).retrieve().bodyToMono(String::class.java).block()
        return objectMapper.readValue(objectMapper.readTree(doc).get("data").get("user").get("edge_owner_to_timeline_media").toString())
    }

    private fun randomRange(n1: Int, n2: Int): Int {
        return (Math.random() * (n2 - n1 + 1)).toInt() + n1
    }
}