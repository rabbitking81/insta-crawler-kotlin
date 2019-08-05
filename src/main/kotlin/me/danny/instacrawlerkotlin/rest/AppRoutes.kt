package me.danny.instacrawlerkotlin.rest

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
    fun apiRouter(apiHandler: InstaAccountApiHandler, instaMediaCrawlingApiHandler: InstaMediaCrawlingApiHandler) = router {
        (accept(MediaType.APPLICATION_JSON_UTF8) and "/api").nest {
            "/insta/account".nest {
                GET("/find", apiHandler::findInstaAccount)
                POST("/", apiHandler::addInstaAccount)
                GET("/", apiHandler::list)
            }

            "/insta/media".nest {
                POST("/", instaMediaCrawlingApiHandler::startCrawling)
                POST("/detail", instaMediaCrawlingApiHandler::startMediaDetailCrawling)
                POST("/all", instaMediaCrawlingApiHandler::allCrawling)
            }

            "/insta/tag".nest {
                GET("/crawling", instaMediaCrawlingApiHandler::testCrawlingByTag)
            }
        }
    }
}