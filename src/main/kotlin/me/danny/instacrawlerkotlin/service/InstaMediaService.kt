package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.danny.instacrawlerkotlin.model.Edge
import me.danny.instacrawlerkotlin.model.EdgeOwnerToTimelineMedia
import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.entity.InstaMediaDetailHistory
import me.danny.instacrawlerkotlin.repository.InstaMediaRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 *
 * Created by rabbitking81 on 2019-05-26.
 *
 * @author rabbitking81
 * @since
 */
@Service
class InstaMediaService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaMediaRepository: InstaMediaRepository) : ILogging by LoggingImp<InstaMediaService>() {
    @Autowired
    lateinit var webClientBuilder: WebClient.Builder

    @Autowired
    lateinit var instaAccountService: InstaAccountService

    @Autowired
    lateinit var instaMediaDetailHistoryService: InstaMediaDetailHistoryService

    @Autowired
    lateinit var instaMediaTagService: InstaMediaTagService

    private val instaLogger = LoggerFactory.getLogger("insta")
    val objectMapper = jacksonObjectMapper()

    fun crawlingMediaByUser(userId: Long): Mono<InstaAccount> {
        return instaAccountService.findInstaAccountByUserId(userId)
            .flatMap {
                if (it.status == "READY") {
                    startCrawlingAsync(it)
                }

                Mono.just(it)
            }
    }

    fun crawlingAll(): Mono<Void> {
        startCrawlingAll()

        return Mono.empty()
    }

    private fun startCrawlingAll() {
        log.info("start crawling media detail")

        instaAccountService.listEntity()
            .map { account ->
                account.status = "CRAWLING"
                instaAccountService.updateInstaAccount(account)
            }
            .flatMap { instaAccount ->
                getMediaFromInsta(instaAccount).map {
                    instaAccount
                }
            }
            .map { account ->
                account.status = "READY"
                instaAccountService.updateInstaAccount(account)
            }
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.info("stop media detail")
            }
    }


    private fun startCrawlingAsync(account: InstaAccount) {
        log.info("start crawling: {}", account.toString())

        Mono.just(account)
            .map {
                account.status = "CRAWLING"
                instaAccountService.updateInstaAccount(account)
            }
            .flatMap { instaAccount ->
                getMediaFromInsta(instaAccount).map {
                    it
                }
            }
            .map {
                account.status = "READY"
                instaAccountService.updateInstaAccount(account)
            }
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.info("stop crawling: {}", it.toString())
            }
    }

    private fun getMediaFromInsta(instaAccount: InstaAccount): Mono<EdgeOwnerToTimelineMedia> {
        return Mono.just(instaAccount)
            .map { instaAccount ->
                val latestMedia = instaMediaRepository.findLatestMediaByUserId(instaAccount.id!!)?.instaMediaId?.toLong()
                    ?: run { 0L }
                log.info("---- latest {}", latestMedia)
                var edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia? = null

                do {
                    edgeOwnerToTimelineMedia = getEdgeOwnerToTimelineMedia(instaAccount.instaAccountId, edgeOwnerToTimelineMedia?.pageInfo?.endCursor)
                    if (!saveInstaMedia(edgeOwnerToTimelineMedia.edges, latestMedia, instaAccount.id)) {
                        edgeOwnerToTimelineMedia.pageInfo.hasNextPage = false
                    }

                    Thread.sleep(10000)
                } while (edgeOwnerToTimelineMedia?.pageInfo?.hasNextPage ?: run { false })

                edgeOwnerToTimelineMedia
            }
    }

    private fun saveInstaMedia(edges: List<Edge>, latestMedia: Long, userId: Long): Boolean {
        for (edge in edges) {
            if (latestMedia >= edge.node.id.toLong()) {
                return false
            }

            val instaMedia = instaMediaRepository.save(edge.node.toInstaMedia(userId))
            val mediaDetailHistory = edge.node.toInstaMediaDetailHistory(instaMedia.id!!)

            if (edge.node.edgeMediaToCaption.edges.isNotEmpty()) {
                instaMediaTagService.saveTags(instaMedia.id, edge.node.edgeMediaToCaption.edges[0].node.text)
            }

            instaLogger.debug(objectMapper.writeValueAsString(mediaDetailHistory))
            instaMediaDetailHistoryService.save(mediaDetailHistory)
        }

        return true
    }

    private fun getEdgeOwnerToTimelineMedia(accountId: Long, endCursor: String? = null): EdgeOwnerToTimelineMedia {
        val objectMapper = jacksonObjectMapper()
        var url = "https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=$accountId&first=50"
        if (endCursor != null) {
            url = "$url&after=$endCursor"
        }

        log.info("insta media url: {}", url)

        val doc = webClientBuilder.build().get().uri(url).retrieve().bodyToMono(String::class.java).block()
        return objectMapper.readValue(objectMapper.readTree(doc).get("data").get("user").get("edge_owner_to_timeline_media").toString())
    }

    fun getInstaMediasDetailHistory(accountId: Long): List<InstaMediaDetailHistory> {
        val edgeList: ArrayList<Edge> = arrayListOf()

        var edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia? = null

        do {
            edgeOwnerToTimelineMedia = getEdgeOwnerToTimelineMedia(accountId, edgeOwnerToTimelineMedia?.pageInfo?.endCursor)
            for (edge in edgeOwnerToTimelineMedia.edges) {
                val instaCreatedAt = edge.node.takenAtTimestamp.time * 1000

                if ((System.currentTimeMillis() - instaCreatedAt)/1000 > (38 * (24 * 60 * 60))) {
                    edgeOwnerToTimelineMedia.pageInfo.hasNextPage = false
                    break
                }

                edgeList.add(edge)
            }

            Thread.sleep(6000)
        } while (edgeOwnerToTimelineMedia?.pageInfo?.hasNextPage ?: run { false })

        val instaMediaDetailHistoryList: ArrayList<InstaMediaDetailHistory> = arrayListOf()

        for (edge in edgeList) {
            val instaMedia = instaMediaRepository.findInstaMediaId(edge.node.id)
            instaMedia?.let {
                val detail = InstaMediaDetailHistory(mediaId = instaMedia.id!!, likeCount = edge.node.edgeMediaPreviewLike.count.toInt(), commentCount = edge.node.edgeMediaToComment.count.toInt(), instaCreatedDate = edge.node.takenAtTimestamp)
                instaLogger.debug(objectMapper.writeValueAsString(detail))
                instaMediaDetailHistoryList.add(detail)
            }
        }

        return instaMediaDetailHistoryList
    }
}