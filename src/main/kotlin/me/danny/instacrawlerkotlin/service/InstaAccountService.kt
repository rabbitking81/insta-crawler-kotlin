package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.danny.instacrawlerkotlin.model.InstaUser
import me.danny.instacrawlerkotlin.model.dto.InstaAccountDto
import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.entity.InstaAccountDetailHistory
import me.danny.instacrawlerkotlin.model.entity.InstaMediaDetailHistory
import me.danny.instacrawlerkotlin.repository.InstaAccountDetailRepository
import me.danny.instacrawlerkotlin.repository.InstaRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import javax.transaction.Transactional

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Service
class InstaAccountService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaRepository: InstaRepository, val instaAccountDetailRepository: InstaAccountDetailRepository) : ILogging by LoggingImp<InstaAccountService>() {

    @Autowired
    lateinit var webClientBuilder: WebClient.Builder

    fun findInstaAccount(username: String, accountType: Int): Mono<InstaAccountDto> {
        return jdbcAsyncUtils.asyncMono { Mono.just(findInstaAccountCrawling(username, accountType).toInstaAccountDto()) }
    }

    fun findInstaAccountByUserId(id: Long): Mono<InstaAccount> {
        return instaRepository.findById(id).map { Mono.just(it) }.orElseGet { Mono.empty() }
    }

    fun addInstaAccount(username: String, accountType: Int): Mono<InstaAccountDto> {
        return jdbcAsyncUtils.asyncMono {
            val instaAccount = findInstaAccountCrawling(username, accountType)
            val entity = instaRepository.save(instaAccount)

            val instaDetail = getInstaAccountDetailCrawling(entity.id!!, entity.instaAccountName)
            instaAccountDetailRepository.save(instaDetail)

            Mono.just(entity.toInstaAccountDto())
        }
    }

    fun listEntity(): Flux<InstaAccount> {
        return jdbcAsyncUtils.asyncFlux {
            Flux.fromIterable(instaRepository.findAll())
        }
    }

    fun list(): Flux<InstaAccountDto> {
        return jdbcAsyncUtils.asyncFlux {
            Flux.fromIterable(instaRepository.findAll().map { it.toInstaAccountDto() })
        }
    }

    fun updateInstaAccount(instaAccount: InstaAccount): InstaAccount {
        return instaRepository.save(instaAccount)
    }

    private fun findInstaAccountCrawling(account: String, accountType: Int): InstaAccount {
        val doc = Jsoup.connect("https://www.instagram.com/$account/?__a=1")
            .ignoreContentType(true)
            .execute().body()

        val mapper = jacksonObjectMapper()
        val result = mapper.readTree(doc).get("graphql").get("user")

        return InstaAccount(accountId = result.get("id").asLong(),
            accountName = result.get("username").asText(),
            fullName = result.get("full_name").asText(), status = "INIT",
            accountType = accountType, country = "", isCrawling = true)
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

    fun getInstaAccountById(ownerId: String): InstaUser {
        val objectMapper = jacksonObjectMapper()
        val url = "https://i.instagram.com/api/v1/users/$ownerId/info/"

        val doc = webClientBuilder.build().get().uri(url).retrieve().bodyToMono(String::class.java).block()
        return objectMapper.readValue(objectMapper.readTree(doc).get("user").toString())
    }

    @Transactional
    fun getUserAndSave(instaUser: InstaUser): InstaAccount {
        var instaAccount = instaRepository.findByInstaAccountId(instaUser.pk)
        if (instaAccount == null) {
            instaAccount = instaRepository.save(InstaAccount(accountId = instaUser.pk,
                accountName = instaUser.username,
                fullName = instaUser.fullName, status = "INIT",
                accountType = 50, country = instaUser.publicPhoneCountryCode, isCrawling = false))
        }

        instaAccountDetailRepository.save(InstaAccountDetailHistory(userId = instaAccount.id!!,
            followCount = instaUser.followerCount,
            followedCount = instaUser.followingCount,
            mediaCount = instaUser.mediaCount,
            isPrivacy = instaUser.isPrivate))

        return instaAccount
    }
}