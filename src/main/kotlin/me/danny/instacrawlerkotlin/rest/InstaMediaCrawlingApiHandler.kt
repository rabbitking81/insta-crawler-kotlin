package me.danny.instacrawlerkotlin.rest

import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.form.InstaMediaForm
import me.danny.instacrawlerkotlin.service.InstaMediaDetailHistoryService
import me.danny.instacrawlerkotlin.service.InstaMediaService
import me.danny.instacrawlerkotlin.service.InstaSourceTagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.json
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
class InstaMediaCrawlingApiHandler {
    @Autowired
    lateinit var instaMediaService: InstaMediaService

    @Autowired
    lateinit var instaMediaDetailHistoryService: InstaMediaDetailHistoryService

    @Autowired
    lateinit var instaSourceTagService: InstaSourceTagService

    fun startCrawling(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(InstaMediaForm::class.java)
            .flatMap {
                ok().json().body(instaMediaService.crawlingMediaByUser(it.userId), InstaAccount::class.java)
            }
    }

    fun startMediaDetailCrawling(request: ServerRequest): Mono<ServerResponse> {
        return instaMediaDetailHistoryService.instaMediaDetailCrawling()
            .flatMap {
                ok().json().build()
            }
    }

    fun allCrawling(request: ServerRequest): Mono<ServerResponse> {
        return instaMediaService.crawlingAll()
            .flatMap {
                ok().json().build()
            }
    }

    fun testCrawlingByTag(request: ServerRequest): Mono<ServerResponse> {
        return instaSourceTagService.testCrawling()
            .flatMap { ok().json().build()}
//
//        return request.bodyToMono(InstaMediaForm::class.java)
//            .flatMap {
//                ok().json().body(instaMediaService.crawlingMediaByUser(it.userId), InstaAccount::class.java)
//            }
    }
}