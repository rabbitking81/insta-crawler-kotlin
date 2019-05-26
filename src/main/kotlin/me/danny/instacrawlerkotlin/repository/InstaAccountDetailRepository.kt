package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.entity.InstaAccountDetailHistory
import org.springframework.data.jpa.repository.JpaRepository

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
interface InstaAccountDetailRepository : JpaRepository<InstaAccountDetailHistory, Long>