package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import org.springframework.data.jpa.repository.JpaRepository

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
interface InstaRepository : JpaRepository<InstaAccount, Long>