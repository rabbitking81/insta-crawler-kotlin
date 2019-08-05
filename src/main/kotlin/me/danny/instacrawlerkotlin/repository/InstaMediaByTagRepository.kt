package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaMediaByTag
import me.danny.instacrawlerkotlin.model.entity.InstaSourceTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface InstaMediaByTagRepository : JpaRepository<InstaMediaByTag, Long> {
    @Query(value = "select * from insta_media_by_tag where insta_media_id = :instaMediaId", nativeQuery = true)
    fun findByInstaMediaId(@Param(value = "instaMediaId") instaMediaId: String): InstaMediaByTag?

}