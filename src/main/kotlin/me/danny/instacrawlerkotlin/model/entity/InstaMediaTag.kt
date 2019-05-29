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
@Table(name = "insta_media_tag")
data class InstaMediaTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "media_id", nullable = false)
    val mediaId: Long,

    @Column(name = "tag_name", nullable = false)
    val tagName: String
) {
    constructor(mediaId: Long, tagName: String) : this(null, mediaId, tagName)
}

