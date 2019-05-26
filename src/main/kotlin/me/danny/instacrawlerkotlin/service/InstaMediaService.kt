package me.danny.instacrawlerkotlin.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.danny.instacrawlerkotlin.model.EdgeOwnerToTimelineMedia
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 *
 * Created by rabbitking81 on 2019-05-26.
 *
 * @author rabbitking81
 * @since
 */
@Service
class InstaMediaService(val jdbcAsyncUtils: JdbcAsyncUtils) {
    fun crawlingMediaByUser(accountId: Long, currentMaxId: Long? = null): Mono<EdgeOwnerToTimelineMedia>  {
        var doc = Jsoup.connect("https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=$accountId&first=10")
            .ignoreContentType(true)
            .execute().body()
        val mapper = jacksonObjectMapper()

//        while(true) {
            val edgeOwnerToTimelineMedia = mapper.readTree(doc).get("data").get("user").get("edge_owner_to_timeline_media")

        val model = mapper.readValue<EdgeOwnerToTimelineMedia>(edgeOwnerToTimelineMedia.toString())

            return Mono.just(model)

//            if(model.pageInfo.hasNextPage == flase) {
//                break
//            } else {
//                doc = Jsoup.connect("https://www.instagram.com/graphql/query/?query_id=17888483320059182&id=$accountId&first=10&after=$endCursor")
//                    .ignoreContentType(true)
//                    .execute().body()
//            }
//        }


    }

}