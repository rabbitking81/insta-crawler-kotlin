package me.danny.instacrawlerkotlin.rest

import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.form.InstaMediaForm
import me.danny.instacrawlerkotlin.service.InstaMediaService
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
class InstaMediaCrawlingApiHandler {
    @Autowired
    lateinit var instaMediaService: InstaMediaService

    fun startCrawling(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(InstaMediaForm::class.java)
            .flatMap {
                ok().json().body(instaMediaService.crawlingMediaByUser(it.userId), InstaAccount::class.java)
            }
    }
}