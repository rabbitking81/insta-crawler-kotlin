package me.danny.instacrawlerkotlin.model.entity

import javax.persistence.*

/**
 *
 * Created by danny.ban on 2019-08-14.
 *
 * @author danny.ban
 * @since
 */
@Entity
@Table(name = "gelato_insta_account_bulk")
data class GelatoInstaAccountBulk(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "shop_id", nullable = true)
    val shopId: Long?,

    @Column(name = "account_name")
    val accountName: String,

    @Column(name = "account_id")
    val accountId: String,

    @Column(name = "followed_by_count")
    val followedByCount: Int,

    @Column(name = "follows_count")
    val followsCount: Int,

    @Column(name = "media_count")
    val mediaCount: Int,

    @Column(name = "is_insta_crawling")
    var isInstaCrawling: Boolean,

    @Column(name = "result")
    var result: String?,

    @Column(name = "end_cursor")
    var endCursor: String?
)