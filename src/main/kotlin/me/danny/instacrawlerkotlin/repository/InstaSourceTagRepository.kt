package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaSourceTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface InstaSourceTagRepository : JpaRepository<InstaSourceTag, Long> {
    @Query(value = "select * from insta_source_tag where status = 'READY' and is_crawling = true", nativeQuery = true)
    fun findCrawlingSourceTag(): List<InstaSourceTag>?
}