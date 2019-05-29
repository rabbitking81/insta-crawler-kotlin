package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaMedia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
interface InstaMediaRepository : JpaRepository<InstaMedia, Long> {
    @Query(value = "select * from insta_media where user_id = :user_id order by insta_created_date desc limit 1", nativeQuery = true)
    fun findLatestMediaByUserId(@Param(value = "user_id") userId: Long): InstaMedia?
}