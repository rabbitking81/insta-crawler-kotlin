package me.danny.instacrawlerkotlin.service

import me.danny.instacrawlerkotlin.model.entity.InstaMediaTag
import me.danny.instacrawlerkotlin.repository.InstaMediaRepository
import me.danny.instacrawlerkotlin.repository.InstaMediaTagRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.springframework.stereotype.Service

/**
 *
 * Created by rabbitking81 on 2019-05-30.
 *
 * @author rabbitking81
 * @since
 */
@Service
class InstaMediaTagService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaMediaTagRepository: InstaMediaTagRepository) : ILogging by LoggingImp<InstaMediaTagService>() {
    fun saveTags(mediaId: Long, text: String) {
        instaMediaTagRepository.saveAll(parserTag(mediaId, text))
    }

    private fun parserTag(mediaId: Long, text: String): ArrayList<InstaMediaTag> {
        val tags: ArrayList<InstaMediaTag> = arrayListOf()

        text.split(" ").filter {
            it.startsWith("#")
        }.map {
            tags.add(InstaMediaTag(mediaId, it.removePrefix("#")))
        }

        return tags
    }
}