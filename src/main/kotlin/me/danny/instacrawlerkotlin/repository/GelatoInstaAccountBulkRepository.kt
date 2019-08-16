package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.GelatoInstaAccount
import me.danny.instacrawlerkotlin.model.entity.GelatoInstaAccountBulk
import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.entity.InstaMediaByTag
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
interface GelatoInstaAccountBulkRepository : JpaRepository<GelatoInstaAccountBulk, Long> {
    @Query(value = "select * from gelato_insta_account_bulk where is_insta_crawling = false and result is null limit 1", nativeQuery = true)
    fun findByIsCrawlingTrue(): GelatoInstaAccountBulk?
}