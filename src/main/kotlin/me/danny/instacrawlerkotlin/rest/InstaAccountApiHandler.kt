package me.danny.instacrawlerkotlin.rest

import me.danny.instacrawlerkotlin.model.dto.InstaAccountDto
import me.danny.instacrawlerkotlin.model.form.InstaAccountForm
import me.danny.instacrawlerkotlin.service.GelatoInstaAccountService
import me.danny.instacrawlerkotlin.service.InstaAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Service
class InstaAccountApiHandler {
    @Autowired
    lateinit var instaAccountService: InstaAccountService

    @Autowired
    lateinit var gelatoInstaAccountService: GelatoInstaAccountService

    fun findInstaAccount(request: ServerRequest): Mono<ServerResponse> {
        val account = request.queryParam("account").get()
        val accountType = request.queryParam("accountType").get().toInt()
        return ok().json().body(instaAccountService.findInstaAccount(account, accountType), InstaAccountDto::class.java)
    }

    fun addInstaAccount(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(InstaAccountForm::class.java)
            .flatMap {
                instaAccountService.addInstaAccount(it.userName, it.accountType).flatMap {
                    ok().json().body(Mono.just(it), InstaAccountDto::class.java)
                }
            }
    }

    fun list(request: ServerRequest): Mono<ServerResponse> {
        return ok().json()
            .body(instaAccountService.list(), InstaAccountDto::class.java)
    }

    fun crawlingForGelatoInstaAccount(request: ServerRequest) : Mono<ServerResponse>{
        gelatoInstaAccountService.startCrawling()
        return ok().json().build()
    }
}