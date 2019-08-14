package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.danny.instacrawlerkotlin.repository.GelatoInstaAccountRepository
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.util.function.Tuple3
import reactor.util.function.Tuples

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
    private lateinit var gelatoInstaAccountRepository: GelatoInstaAccountRepository

    @Async
    fun startCrawling() {
        while (true) {
            val targetAccount = gelatoInstaAccountRepository.findByIsCrawlingTrue() ?: break

            val instaCount = getInstaAccountDetailCrawling(targetAccount.accountName)
            targetAccount.instaFollowedByCount = instaCount.t1
            targetAccount.instaFollowsCount = instaCount.t2
            targetAccount.instaMediaCount = instaCount.t3
            targetAccount.isInstaCrawling = true

            gelatoInstaAccountRepository.save(targetAccount)

            Thread.sleep(randomRange(5, 9) * 1000L)
        }
    }

    private fun getInstaAccountDetailCrawling(accountName: String): Tuple3<Int, Int, Int> {
        val doc = Jsoup.connect("https://www.instagram.com/$accountName/?__a=1")
            .ignoreContentType(true)
            .execute().body()

        val mapper = jacksonObjectMapper()
        val result = mapper.readTree(doc).get("graphql").get("user")

        val followedCount = result.get("edge_followed_by").get("count").asInt()
        val followCount = result.get("edge_follow").get("count").asInt()
        val mediaCount = result.get("edge_owner_to_timeline_media").get("count").asInt()

        return Tuples.of(followedCount, followCount, mediaCount)
    }

    private fun randomRange(n1: Int, n2: Int): Int {
        return (Math.random() * (n2 - n1 + 1)).toInt() + n1
    }
}