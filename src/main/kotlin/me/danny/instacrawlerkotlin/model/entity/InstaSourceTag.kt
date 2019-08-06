package me.danny.instacrawlerkotlin.model.entity

import java.sql.Timestamp
import javax.persistence.*

/**
 *
 * Created by rabbitking81 on 2019-08-03.
 *
 * @author rabbitking81
 * @since
 */
@Entity
@Table(name = "insta_source_tag")
data class InstaSourceTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "status", nullable = false)
    var status: String,

    @Column(name = "created_date", nullable = false)
    val createdDate: Timestamp,

    @Column(name = "is_crawling", nullable = false)
    val isCrawling: Boolean,

    @Column(name = "end_cursor")
    var endCursor: String?
)