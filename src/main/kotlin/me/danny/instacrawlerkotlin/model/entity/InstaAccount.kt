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
    var status: String,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp,

    @Column(name = "account_type", nullable = false)
    val accountType: Int,

    @Column(name = "country")
    var country: String?,

    @Column(name = "is_crawling", nullable = false)
    var isCrawling: Boolean
) {
    constructor(accountId: Long, accountName: String, fullName: String, status: String, accountType: Int, country: String?, isCrawling: Boolean)
        : this(null, accountId, accountName, fullName, status, Timestamp(System.currentTimeMillis()), accountType, country, isCrawling)

    fun toInstaAccountDto() = instaAccountToInstaAccountDto.transform(this)
}

