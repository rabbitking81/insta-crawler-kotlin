package me.danny.instacrawlerkotlin.rest

import me.danny.instacrawlerkotlin.model.dto.InstaAccountDto
import me.danny.instacrawlerkotlin.model.form.InstaAccountForm
import me.danny.instacrawlerkotlin.service.GelatoInstaAccountService
import me.danny.instacrawlerkotlin.service.GelatoInstaMediaService
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
class GelatoInstaApiHandler {

    @Autowired
    lateinit var gelatoInstaAccountService: GelatoInstaAccountService

    @Autowired
    lateinit var gelatoInstaMediaService: GelatoInstaMediaService

    fun crawlingForGelatoInstaAccount(request: ServerRequest) : Mono<ServerResponse>{
        gelatoInstaAccountService.startCrawling()
        return ok().json().build()
    }

    fun crawlingForGelatoInstaMedia(request: ServerRequest) : Mono<ServerResponse>{
        gelatoInstaMediaService.startCrawling()
        return ok().json().build()
    }
}