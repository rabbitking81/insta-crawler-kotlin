package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Repository
interface GelatoInstaMediaBulkRepository : JpaRepository<GelatoInstaMediaBulk, Long> {
    @Query(value = "select * from gelato_insta_media_bulk where source_content_id like CONCAT(:content_id,'%') limit 1", nativeQuery = true)
    fun findByContentId(@Param(value = "content_id") content_id: String): GelatoInstaMediaBulk?
}