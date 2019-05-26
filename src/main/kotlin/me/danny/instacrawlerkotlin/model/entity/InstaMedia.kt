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
@Table(name = "insta_media")
data class InstaMedia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "short_code", nullable = false)
    val shortCode: String,

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
    constructor(shortCode: String, imageUrl: String, instaType: String, userId: Long?, instaCreatedDate: Timestamp) : this(null, shortCode, imageUrl, instaType, userId, instaCreatedDate, Timestamp(System.currentTimeMillis()))
}

