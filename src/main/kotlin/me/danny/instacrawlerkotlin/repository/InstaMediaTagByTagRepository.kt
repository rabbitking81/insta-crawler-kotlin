package me.danny.instacrawlerkotlin.repository

import me.danny.instacrawlerkotlin.model.entity.InstaMediaTagByTag
import org.springframework.data.jpa.repository.JpaRepository

interface InstaMediaTagByTagRepository : JpaRepository<InstaMediaTagByTag, Long>