package me.danny.instacrawlerkotlin

import org.springframework.data.jpa.repository.JpaRepository

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
interface InstaRepository : JpaRepository<InstaAccount, Long>