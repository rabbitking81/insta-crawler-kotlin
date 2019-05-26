package me.danny.instacrawlerkotlin.model.entity

import me.danny.instacrawlerkotlin.model.instaAccountToInstaAccountDto
import java.sql.Timestamp
import javax.persistence.*

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Entity
@Table(name = "insta_account")
data class InstaAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "insta_account_id", nullable = false)
    val instaAccountId: Long,

    @Column(name = "insta_account_name", nullable = false)
    val instaAccountName: String,

    @Column(name = "insta_full_name", nullable = false)
    val instaFullName: String,

    @Column(name = "status", nullable = false)
    val status: String,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp
) {
    constructor(accountId: Long, accountName: String, fullName: String, status: String) : this(null, accountId, accountName, fullName, status, Timestamp(System.currentTimeMillis()))

    fun toInstaAccountDto() = instaAccountToInstaAccountDto.transform(this)
}

