package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaAccountDetailHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.sql.Timestamp

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
interface InstaAccountDetailRepository : JpaRepository<InstaAccountDetailHistory, Long> {
    @Query(value = "select * from insta_account_detail_history where user_id = :userId and calculated_date = :calculatedDate", nativeQuery = true)
    fun findByInstaAccountIdAndCalculated(@Param(value = "userId") userId: Long, @Param(value = "calculatedDate") calculatedDate: Timestamp): InstaAccountDetailHistory?
}