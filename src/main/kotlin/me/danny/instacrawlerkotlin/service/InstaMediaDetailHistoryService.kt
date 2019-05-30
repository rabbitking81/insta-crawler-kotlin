package me.danny.instacrawlerkotlin.service

import me.danny.instacrawlerkotlin.model.entity.InstaMediaDetailHistory
import me.danny.instacrawlerkotlin.repository.InstaMediaDetailHistoryRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 *
 * Created by danny.ban on 2019-05-29.
 *
 * @author danny.ban
 * @since
 */
@Service
class InstaMediaDetailHistoryService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaMediaDetailHistoryRepository: InstaMediaDetailHistoryRepository) : ILogging by LoggingImp<InstaMediaDetailHistoryService>() {
    @Autowired
    lateinit var instaAccountService: InstaAccountService

    @Autowired
    lateinit var instaMediaService: InstaMediaService

    private val instaLogger = LoggerFactory.getLogger("insta")

    fun save(instaMediaDetailHistory: InstaMediaDetailHistory): InstaMediaDetailHistory {
        return instaMediaDetailHistoryRepository.save(instaMediaDetailHistory)
    }

    fun instaMediaDetailCrawling(): Mono<Void> {
        startInstaMediaDetailCrawling()

        return Mono.empty()
    }

    private fun startInstaMediaDetailCrawling() {
        log.info("start crawling media detail")

        instaAccountService.list()
            .map {
                log.info("start crawling media detail: {}", it.instaAccountName)

                instaMediaDetailHistoryRepository.saveAll(instaMediaService.getInstaMediasDetailHistory(it.instaAccountId))
            }
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.info("stop media detail")
            }
    }
}
