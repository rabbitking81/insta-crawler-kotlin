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
@Table(name = "insta_account_detail_history")
data class InstaAccountDetailHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "follow_count", nullable = false)
    val followCount: Int,

    @Column(name = "followed_count", nullable = false)
    val followedCount: Int,

    @Column(name = "media_count", nullable = false)
    val mediaCount: Int,

    @Column(name = "is_privacy", nullable = false)
    val isPrivacy: Boolean,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp,

    @Column(name = "calculated_date")
    val calculatedDate: Timestamp?
) {
    constructor(userId: Long, followCount: Int, followedCount: Int, mediaCount: Int, isPrivacy: Boolean, calculatedDate: Timestamp?) : this(null, userId, followCount, followedCount, mediaCount, isPrivacy, Timestamp(System.currentTimeMillis()), calculatedDate)

//    fun toInstaAccountDto() = instaAccountToInstaAccountDto.transform(this)
}

