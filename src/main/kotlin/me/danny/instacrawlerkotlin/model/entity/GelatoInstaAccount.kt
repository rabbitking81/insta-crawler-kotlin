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
@Table(name = "gelato_insta_account")
data class GelatoInstaAccount(
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

    @Column(name = "insta_followed_by_count")
    var instaFollowedByCount: Int,

    @Column(name = "insta_follows_count")
    var instaFollowsCount: Int,

    @Column(name = "insta_media_count")
    var instaMediaCount: Int
)