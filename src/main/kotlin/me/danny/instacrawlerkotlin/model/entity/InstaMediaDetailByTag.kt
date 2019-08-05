package me.danny.instacrawlerkotlin.model.entity

import java.sql.Timestamp
import javax.persistence.*

/**
 *
 * Created by rabbitking81 on 2019-08-04.
 *
 * @author rabbitking81
 * @since
 */
@Entity
@Table(name = "insta_media_detail_by_tag")
data class InstaMediaDetailByTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "media_id", nullable = false)
    val mediaId: Long,

    @Column(name = "like_count", nullable = false)
    val likeCount: Long,

    @Column(name = "comment_count", nullable = false)
    val commentCount: Long,

    @Column(name = "calculated_date", nullable = false)
    val calculatedDate: Timestamp,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp
)