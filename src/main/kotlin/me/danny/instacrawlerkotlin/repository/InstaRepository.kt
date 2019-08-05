package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.entity.InstaMediaByTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
interface InstaRepository : JpaRepository<InstaAccount, Long> {
    @Query(value = "select * from insta_account where insta_account_id = :instaAccountId", nativeQuery = true)
    fun findByInstaAccountId(@Param(value = "instaAccountId") instaAccountId: Long): InstaAccount?
}