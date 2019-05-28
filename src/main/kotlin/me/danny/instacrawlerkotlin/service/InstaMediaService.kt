package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.danny.instacrawlerkotlin.model.EdgeOwnerToTimelineMedia
import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.repository.InstaMediaDetailHistoryRepository
import me.danny.instacrawlerkotlin.repository.InstaMediaRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
class InstaMediaService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaMediaRepository: InstaMediaRepository, val instaMediaDetailHistoryRepository: InstaMediaDetailHistoryRepository) : ILogging by LoggingImp<InstaMediaService>() {
    @Autowired
    lateinit var instaAccountService: InstaAccountService

    fun crawlingMediaByUser(userId: Long): Mono<InstaAccount> {
        return instaAccountService.findInstaAccountByUserId(userId)
            .flatMap {
                if (it.status == "READY") {
                    startCrawling(it)
                }

                Mono.just(it)
            }
    }

    private fun startCrawling(account: InstaAccount) {
        Mono.just(account)
            .map {
                account.status = "CRAWLING"
                instaAccountService.updateInstaAccount(account)
            }
            .map {
                var endCursor: String? = null

                do {
                    val model = getEdgeOwnerToTimelineMedia(it.instaAccountId, endCursor)
                    endCursor = model.pageInfo.endCursor

                    for (media in model.edges) {
                        val instaMedia = instaMediaRepository.save(media.node.toInstaMedia(account.id))
                        val mediaDetailHistory = media.node.toInstaMediaDetailHistory(instaMedia.id!!)
                        instaMediaDetailHistoryRepository.save(mediaDetailHistory)
                    }
                } while (model.pageInfo.hasNextPage)

                it
            }
            .map {
                account.status = "READY"
                instaAccountService.updateInstaAccount(account)
            }
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.debug("stop crawling: {}", it.toString())
            }
    }

    private fun getEdgeOwnerToTimelineMedia(accountId: Long, endCursor: String? = null): EdgeOwnerToTimelineMedia {
        var url = "https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=$accountId&first=50"
        if (endCursor != null) {
            url = "$url&after=$endCursor"
        }

        log.debug("insta media url: {}", url)

        var doc = Jsoup.connect(url)
            .ignoreContentType(true)
            .execute().body()
        val mapper = jacksonObjectMapper()
        val edgeOwnerToTimelineMedia = mapper.readTree(doc).get("data").get("user").get("edge_owner_to_timeline_media")
        return mapper.readValue(edgeOwnerToTimelineMedia.toString())
    }
}