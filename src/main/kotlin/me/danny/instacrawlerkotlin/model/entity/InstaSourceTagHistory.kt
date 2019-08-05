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
@Table(name = "insta_source_tag_history")
data class InstaSourceTagHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "source_tag_id", nullable = false)
    val sourceTagId: Long,

    @Column(name = "start_at", nullable = false)
    val start_at: Timestamp,

    @Column(name = "end_at")
    var end_at: Timestamp? = null,

    @Column(name = "cnt")
    var cnt: Int = 0
) {
    constructor(sourceTagId: Long) : this(null, sourceTagId, Timestamp(System.currentTimeMillis()))
}