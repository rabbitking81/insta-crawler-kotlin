package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaMediaDetailByTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 *
 * Created by rabbitking81 on 2019-08-04.
 *
 * @author rabbitking81
 * @since
 */
@Repository
interface InstaMediaDetailByTagRepository : JpaRepository<InstaMediaDetailByTag, Long>
