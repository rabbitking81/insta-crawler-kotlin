package me.danny.instacrawlerkotlin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Configuration
class AppRoutes {
    @Bean
    fun apiRouter(apiHandler: InstaAccountApiHandler) = router {
        (accept(MediaType.APPLICATION_JSON_UTF8) and "/api").nest {
            "/insta/account".nest {
                GET("/find", apiHandler::findInstaAccount)
                POST("/", apiHandler::addInstaAccount)
                GET("/", apiHandler::list)
//                GET("/{id}", userApiHandler::getById)

            }
        }
    }
}