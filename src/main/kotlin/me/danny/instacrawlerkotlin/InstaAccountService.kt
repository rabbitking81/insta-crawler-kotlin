package me.danny.instacrawlerkotlin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
class InstaAccountService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaRepository: InstaRepository) {
    fun findInstaAccount(username: String): Mono<InstaAccountDto> {
        return jdbcAsyncUtils.asyncMono { Mono.just(findInstaAccountCrawling(username).toInstaAccountDto()) }
    }

    fun addInstaAccount(username: String): Mono<InstaAccountDto> {
        return jdbcAsyncUtils.asyncMono {
            val entity = findInstaAccountCrawling(username)
            instaRepository.save(entity)

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
}