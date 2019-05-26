package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.dto.InstaAccountDto
import me.danny.instacrawlerkotlin.model.entity.InstaAccountDetailHistory
import me.danny.instacrawlerkotlin.repository.InstaAccountDetailRepository
import me.danny.instacrawlerkotlin.repository.InstaRepository
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Service
class InstaAccountService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaRepository: InstaRepository, val instaAccountDetailRepository: InstaAccountDetailRepository) {
    fun findInstaAccount(username: String): Mono<InstaAccountDto> {
        return jdbcAsyncUtils.asyncMono { Mono.just(findInstaAccountCrawling(username).toInstaAccountDto()) }
    }

    fun addInstaAccount(username: String): Mono<InstaAccountDto> {
        return jdbcAsyncUtils.asyncMono {
            val instaAccount = findInstaAccountCrawling(username)
            val entity = instaRepository.save(instaAccount)

            val instaDetail = getInstaAccountDetailCrawling(entity.id!!, entity.instaAccountName)
            instaAccountDetailRepository.save(instaDetail)

            Mono.just(entity.toInstaAccountDto())
        }
    }

    fun list(): Flux<InstaAccountDto> {
        return jdbcAsyncUtils.asyncFlux {
            Flux.fromIterable(instaRepository.findAll().map { it.toInstaAccountDto() })
        }
    }

    private fun findInstaAccountCrawling(account: String): InstaAccount {
        val doc = Jsoup.connect("https://www.instagram.com/$account/?__a=1")
            .ignoreContentType(true)
            .execute().body()

        val mapper = jacksonObjectMapper()
        val result = mapper.readTree(doc).get("graphql").get("user")

        return InstaAccount(accountId = result.get("id").asLong(),
            accountName = result.get("username").asText(),
            fullName = result.get("full_name").asText(), status = "READY")
    }

    private fun getInstaAccountDetailCrawling(userId: Long, account: String): InstaAccountDetailHistory {
        val doc = Jsoup.connect("https://www.instagram.com/$account/?__a=1")
            .ignoreContentType(true)
            .execute().body()

        val mapper = jacksonObjectMapper()
        val result = mapper.readTree(doc).get("graphql").get("user")

        val followedCount = result.get("edge_followed_by").get("count").asInt()
        val followCount = result.get("edge_follow").get("count").asInt()
        val mediaCount = result.get("edge_owner_to_timeline_media").get("count").asInt()
        val isPravicy = result.get("is_private").asBoolean()

        return InstaAccountDetailHistory(userId = userId, followCount = followedCount, followedCount = followCount, mediaCount = mediaCount, isPrivacy = isPravicy)
    }
}