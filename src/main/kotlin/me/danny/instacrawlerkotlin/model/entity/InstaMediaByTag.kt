package me.danny.instacrawlerkotlin.model.entity

import java.sql.Timestamp
import javax.persistence.*

/**
 *
 * Created by rabbitking81 on 2019-08-04.
 *
 * @author rabbitking81
 * @since
 *
 * create table insta_media_by_tag
(
id                 bigserial             not null
constraint insta_media_by_tag_pk
primary key,
short_code         varchar(50)           not null,
insta_media_id     varchar(20)           not null,
user_id            bigint                not null,
image_url          varchar(512)          not null,
insta_type         varchar(20)           not null,
is_top             boolean default false not null,
insta_created_date timestamp             not null
);

 */
@Entity
@Table(name = "insta_media_by_tag")
data class InstaMediaByTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "short_code", nullable = false)
    val shortCode: String,

    @Column(name = "insta_media_id", nullable = false)
    val instaMediaId: String,

    @Column(name = "user_id", nullable = true)
    val userId: Long,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,

    @Column(name = "insta_type", nullable = false)
    val instaType: String,

    @Column(name = "is_top", nullable = false)
    val isTop: Boolean,

    @Column(name = "insta_created_date", nullable = false)
    val instaCreatedDate: Timestamp,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp
) {
    constructor(shortCode: String, instaMediaId: String, userId: Long, imageUrl: String, instaType: String, isTop: Boolean, instaCreatedDate: Timestamp)
        : this(null, shortCode, instaMediaId, userId, imageUrl, instaType, isTop, instaCreatedDate, Timestamp(System.currentTimeMillis()))
}
