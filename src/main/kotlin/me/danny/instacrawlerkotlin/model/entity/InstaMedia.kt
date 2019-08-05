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
@Table(name = "insta_media")
data class InstaMedia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "short_code", nullable = false)
    val shortCode: String,

    @Column(name = "insta_media_id", nullable = false)
    val instaMediaId: String,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,


    @Column(name = "insta_type", nullable = false)
    val instaType: String,

    @Column(name = "user_id", nullable = true)
    val userId: Long?,

    @Column(name = "insta_created_date", nullable = false)
    val instaCreatedDate: Timestamp,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp
) {
    constructor(shortCode: String, instaMediaId: String, imageUrl: String, instaType: String, userId: Long?, instaCreatedDate: Timestamp) : this(null, shortCode, instaMediaId, imageUrl, instaType, userId, Timestamp(instaCreatedDate.time * 1000), Timestamp(System.currentTimeMillis()))
}

