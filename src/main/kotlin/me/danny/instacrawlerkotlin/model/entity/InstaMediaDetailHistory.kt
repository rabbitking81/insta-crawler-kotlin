package me.danny.instacrawlerkotlin.model.entity

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
@Table(name = "insta_media_detail_history")
data class InstaMediaDetailHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "media_id", nullable = false)
    val mediaId: Long,

    @Column(name = "like_count", nullable = false)
    val likeCount: Int,

    @Column(name = "comment_count", nullable = false)
    val commentCount: Int,

    @Column(name = "insta_created_date", nullable = false)
    val instaCreatedDate: Timestamp,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp
) {
    constructor(mediaId: Long, likeCount: Int, commentCount: Int, instaCreatedDate: Timestamp)
        : this(null, mediaId, likeCount, commentCount, Timestamp(instaCreatedDate.time * 1000), Timestamp(System.currentTimeMillis()))
}

