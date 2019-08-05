package me.danny.instacrawlerkotlin.scheduler

import me.danny.instacrawlerkotlin.service.InstaMediaDetailHistoryService
import me.danny.instacrawlerkotlin.service.InstaMediaService
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

/**
 *
 * Created by danny.ban on 2019-06-04.
 *
 * @author danny.ban
 * @since
 */
@Component
class BatchInstaCrawler : ILogging by LoggingImp<BatchInstaCrawler>() {
    @Autowired
    lateinit var instaMediaDetailHistoryService: InstaMediaDetailHistoryService

    @Autowired
    lateinit var instaMediaService: InstaMediaService

//    @Scheduled(cron = "0 0 0/6 * * ?")
    fun instaMediaCrawling() {
        log.info("start cron insta media service!!")

        instaMediaDetailHistoryService.instaMediaDetailCrawling()
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.info("cron insta media service!!")
            }
    }

//    @Scheduled(cron = "0 0 0/12 * * ?")
    fun instaMediaCrawlingAll() {
        log.info("== start cron insta media service!!")

        instaMediaService.crawlingAll()
            .subscribeOn(Schedulers.elastic())
            .subscribe {
                log.info("== cron insta media service!!")
            }
    }

}