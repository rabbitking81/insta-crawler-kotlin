package me.danny.instacrawlerkotlin.model.dto

import java.sql.Timestamp

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
data class InstaAccountDto(
    val id: Long? = null,

    val instaAccountId: Long,

    val instaAccountName: String,

    val instaFullName: String,

    val status: String,

    val createdDate: Timestamp
)