package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.danny.instacrawlerkotlin.exception.TagCrawlingException
import me.danny.instacrawlerkotlin.model.Edge
import me.danny.instacrawlerkotlin.model.EdgeHashTagToTopPosts
import me.danny.instacrawlerkotlin.model.EdgeItem
import me.danny.instacrawlerkotlin.model.EdgeOwnerToTimelineMedia
import me.danny.instacrawlerkotlin.model.entity.*
import me.danny.instacrawlerkotlin.repository.*
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuples
import java.sql.Timestamp
import javax.transaction.Transactional


/**
 *
 * Created by rabbitking81 on 2019-08-03.
 *
 * @author rabbitking81
 * @since
 */
@Service
class InstaSourceTagService(val jdbcAsyncUtils: JdbcAsyncUtils,
                            val instaSourceTagRepository: InstaSourceTagRepository,
                            val instaSourceTagHistoryRepository: InstaSourceTagHistoryRepository,
                            val instaMediaByTagRepository: InstaMediaByTagRepository,
                            val instaMediaTagByTagRepository: InstaMediaTagByTagRepository,
                            val instaMediaDetailByTagRepository: InstaMediaDetailByTagRepository) : ILogging by LoggingImp<InstaMediaService>() {

    @Autowired
    lateinit var webClientBuilder: WebClient.Builder

    @Autowired
    lateinit var instaAccountService: InstaAccountService

    fun getSourceTagList(): Flux<InstaSourceTag> {
        return instaSourceTagRepository.findCrawlingSourceTag()?.toFlux() ?: Flux.empty()
    }

    fun testCrawling(): Mono<Void> {
        startSourceTagCrawling()
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.info("stop media detail")
            }

        return Mono.empty()
    }

    @Transactional
    fun startSourceTagCrawling(): Mono<List<InstaSourceTag>> {
        var mInstaSourceTag: InstaSourceTag? = null
        var instaSourceTagHistory: InstaSourceTagHistory? = null

        return getSourceTagList().flatMap { t: InstaSourceTag ->
            mInstaSourceTag = t
            t.endCursor = null
            t.status = "DOING"
            instaSourceTagRepository.save(t)
            instaSourceTagHistory = instaSourceTagHistoryRepository.save(InstaSourceTagHistory(t.id!!))
            Mono.just(Tuples.of(t, instaSourceTagHistory!!))
        }.flatMap {
            // loop 을 돌면서 해당 태그에 해당하는 디자인을 다 긁어온다.
            val totalCount = getInstaMediaByTag(it.t1.name, it.t2.start_at)
            it.t2.cnt = totalCount

            Mono.just(it)
        }.flatMap {
            it.t1.status = "READY"
            instaSourceTagRepository.save(it.t1)

            it.t2.end_at = Timestamp(System.currentTimeMillis())
            instaSourceTagHistoryRepository.save(it.t2)

            Mono.just(it.t1)
        }
            .collectList()
            .doOnError {
                if (it is TagCrawlingException) {
                    val cnt = it.totalCount
                    val endCursor = it.endCursor

                    mInstaSourceTag?.status = "READY"
                    mInstaSourceTag?.endCursor = endCursor
                    instaSourceTagHistory?.end_at = Timestamp(System.currentTimeMillis())

                    mInstaSourceTag?.let { tag ->
                        instaSourceTagRepository.save(tag)
                    }

                    instaSourceTagHistory?.let { history ->
                        instaSourceTagHistoryRepository.save(history)
                    }
                }
            }
    }

    private fun getEdgeOwnerToTimelineMediaByTag(tagName: String, endCursor: String? = null): Pair<EdgeOwnerToTimelineMedia, EdgeHashTagToTopPosts?> {
        val objectMapper = jacksonObjectMapper()
        val includeTop: Boolean = (endCursor == null)

        val url =
            if (endCursor != null) {
                "{\"tag_name\":\"$tagName\",\"first\":0,\"after\":\"$endCursor\"}"
            } else {
                "{\"tag_name\":\"$tagName\",\"first\":0}"
            }

        val factory = DefaultUriBuilderFactory("https://www.instagram.com")
        factory.setEncodingMode(EncodingMode.TEMPLATE_AND_VALUES)
        val client = WebClient.builder().uriBuilderFactory(factory).build()
        val uri = factory.uriString("/graphql/query")
            .queryParam("query_hash", "f92f56d47dc7a55b606908374b43a314")
            .queryParam("variables", "{variables}")
            .build(url)

        val doc = client.get().uri(uri).retrieve().bodyToMono(String::class.java).block()

        var top: EdgeHashTagToTopPosts? = null
        if (includeTop)
            top = getTopMedia(doc)

        val media: EdgeOwnerToTimelineMedia = objectMapper.readValue(objectMapper.readTree(doc).get("data").get("hashtag").get("edge_hashtag_to_media").toString())
        return Pair(media, top)
    }

    private fun getTopMedia(doc: String?): EdgeHashTagToTopPosts {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(objectMapper.readTree(doc).get("data").get("hashtag").get("edge_hashtag_to_top_posts").toString())
    }

    @Transactional
    fun getInstaMediaByTag(tagName: String, calculatedDate: Timestamp): Int {
        var totalCount = 0
        var edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia? = null

        try {
            do {
                val instaMedias = getEdgeOwnerToTimelineMediaByTag(tagName, edgeOwnerToTimelineMedia?.pageInfo?.endCursor)
                edgeOwnerToTimelineMedia = instaMedias.first

                instaMedias.second?.let {
                    totalCount += saveInstaMedias(it.edges, calculatedDate, true)
                }

                totalCount += saveInstaMedias(edgeOwnerToTimelineMedia.edges, calculatedDate, false)
                Thread.sleep(randomRange(5, 9) * 1000L)
            } while (edgeOwnerToTimelineMedia?.pageInfo?.hasNextPage ?: run { false })

            return totalCount
        } catch (e: Exception) {
            throw TagCrawlingException(totalCount, edgeOwnerToTimelineMedia?.pageInfo?.endCursor)
        }
    }

    private fun saveInstaMedias(medias: List<Edge>, calculatedDate: Timestamp, isTop: Boolean): Int {
        var count = 0;

        for (item in medias) {
            var instaAccount = instaAccountService.isNeedInstaAccount(item.node.owner.id, calculatedDate)
            if (instaAccount == null) {
                val instaUser = instaAccountService.getInstaAccountById(ownerId = item.node.owner.id)
                instaAccount = instaAccountService.getUserAndSave(instaUser, calculatedDate)

                Thread.sleep(randomRange(5, 9) * 1000L)
            }

            // 미디어
            saveInstaMediaByTag(item.node, instaAccount, calculatedDate, isTop)

            count += 1
        }

        return count
    }

    private fun saveInstaMediaByTag(media: EdgeItem, instaAccount: InstaAccount, calculatedDate: Timestamp, isTop: Boolean) {
        var instaMediaByTag = instaMediaByTagRepository.findByInstaMediaId(media.id)
        if (instaMediaByTag == null) {
            instaMediaByTag = instaMediaByTagRepository.save(InstaMediaByTag(shortCode = media.shortcode,
                instaMediaId = media.id,
                userId = instaAccount.id!!,
                imageUrl = media.displayUrl,
                instaType = media.__typename,
                isTop = isTop,
                instaCreatedDate = Timestamp(media.takenAtTimestamp.time * 1000)))

            // TAG 처리는 여기서...
            if (media.edgeMediaToCaption.edges.isNotEmpty()) {
                saveTags(instaMediaByTag.id!!, media.edgeMediaToCaption.edges[0].node.text)
            }
        }

        instaMediaDetailByTagRepository.save(InstaMediaDetailByTag(id = null,
            mediaId = instaMediaByTag.id!!,
            likeCount = media.edgeMediaPreviewLike.count,
            commentCount = media.edgeMediaToComment.count,
            calculatedDate = calculatedDate,
            createdDate = Timestamp(System.currentTimeMillis())))
    }

    private fun saveTags(mediaId: Long, text: String) {
        instaMediaTagByTagRepository.saveAll(parserTag(mediaId, text))
    }

    private fun parserTag(mediaId: Long, text: String): ArrayList<InstaMediaTagByTag> {
        val tags: ArrayList<InstaMediaTagByTag> = arrayListOf()

        text.split(" ").filter {
            it.startsWith("#")
        }.map {
            tags.add(InstaMediaTagByTag(mediaId, it.removePrefix("#")))
        }

        return tags
    }

    private fun randomRange(n1: Int, n2: Int): Int {
        return (Math.random() * (n2 - n1 + 1)).toInt() + n1
    }
}