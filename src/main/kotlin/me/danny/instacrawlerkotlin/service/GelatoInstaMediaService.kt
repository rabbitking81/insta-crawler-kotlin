package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.danny.instacrawlerkotlin.repository.GelatoInstaAccountRepository
import me.danny.instacrawlerkotlin.repository.GelatoInstaMediaRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.LoggingImp
import me.danny.instacrawlerkotlin.utils.getInstagramPostId
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.function.*
import java.lang.Exception
import java.sql.Timestamp
import javax.transaction.Transactional

/**
 *
 * Created by danny.ban on 2019-08-14.
 *
 * @author danny.ban
 * @since
 */
@Service
class GelatoInstaMediaService {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    private lateinit var gelatoInstaMediaRepository: GelatoInstaMediaRepository

    @Autowired
    lateinit var webClientBuilder: WebClient.Builder

    @Async
    fun startCrawling() {
        while (true) {
            val target = gelatoInstaMediaRepository.findByIsCrawlingTrue() ?: break

            try {
                val instaCount = getInstaMediaDetailCrawling(getInstagramPostId(target.sourceContentId))
                target.isCrawling = true
                target.instaCrawlingDate = Timestamp(System.currentTimeMillis())
                target.instaCreatedDate = Timestamp(instaCount.t2)
                target.instaLikeCount = instaCount.t3
                target.instaCommentCount = instaCount.t4
                target.imageUrl = instaCount.t5
                target.instaMediaId = instaCount.t1

                gelatoInstaMediaRepository.save(target)
            } catch (e: Exception) {
                logger.error(e.message)

                target.result = "FAILED"
                gelatoInstaMediaRepository.save(target)
            }

            Thread.sleep(randomRange(5, 9) * 1000L)
        }
    }

    private fun getInstaMediaDetailCrawling(shortCode: String): Tuple5<String, Long, Int, Int, String> {
        val url = "https://www.instagram.com/p/$shortCode/?__a=1"
        val doc = webClientBuilder.build().get().uri(url).retrieve().bodyToMono(String::class.java).block()
//
//        val doc = Jsoup.connect("https://www.instagram.com/p/$shortCode/?__a=1")
//            .ignoreContentType(true)
//            .execute().body()

        val mapper = jacksonObjectMapper()
        val result = mapper.readTree(doc).get("graphql").get("shortcode_media")

        val id = result.get("id").asText()
        val instaCreatedDate = result.get("taken_at_timestamp").asLong() * 1000
        val likeCount = result.get("edge_media_preview_like").get("count").asInt()
        val commentCount = result.get("edge_media_preview_comment").get("count").asInt()
        val imageUrl = result.get("display_url").asText()

        return Tuples.of(id, instaCreatedDate, likeCount, commentCount, imageUrl)
    }

    private fun randomRange(n1: Int, n2: Int): Int {
        return (Math.random() * (n2 - n1 + 1)).toInt() + n1
    }
}