package me.danny.instacrawlerkotlin.model.entity

import java.sql.Timestamp
import javax.persistence.*

/**
 *
 * Created by danny.ban on 2019-08-14.
 *
 * @author danny.ban
 * @since
 */
@Entity
@Table(name = "gelato_insta_media_bulk")
data class GelatoInstaMediaBulk(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "source_content_id")
    val sourceContentId: String,

    @Column(name = "view_count")
    val viewCount: Int,

    @Column(name = "scrap_count")
    val scrapCount: Int,

    @Column(name = "detail_capture_count")
    val detailCaptureCount: Int,

    @Column(name = "detail_photo_capture_count")
    val detailPhotoCaptureCount: Int,

    @Column(name = "status")
    val status: String,

    @Column(name = "published_date")
    val publishedDate: Timestamp?,

    @Column(name = "source_channel_account_id")
    val sourceChannelAccountId: Long?,

    @Column(name = "is_crawling")
    var isCrawling: Boolean,

    @Column(name = "image_url")
    var imageUrl: String?,

    @Column(name = "insta_like_count")
    var instaLikeCount: Int,

    @Column(name = "insta_crawling_date")
    var instaCrawlingDate: Timestamp?,

    @Column(name = "design_id")
    val designId: Long,

    @Column(name = "insta_comment_count")
    var instaCommentCount: Int,

    @Column(name = "insta_created_date")
    var instaCreatedDate: Timestamp?,

    @Column(name = "insta_media_id")
    var instaMediaId: String?,

    @Column(name = "result")
    var result: String?
)