package me.danny.instacrawlerkotlin.service

import me.danny.instacrawlerkotlin.model.entity.InstaMediaDetailHistory
import me.danny.instacrawlerkotlin.repository.InstaMediaDetailHistoryRepository
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.JdbcAsyncUtils
import me.danny.instacrawlerkotlin.utils.LoggingImp
import org.springframework.stereotype.Service

/**
 *
 * Created by danny.ban on 2019-05-29.
 *
 * @author danny.ban
 * @since
 */
@Service
class InstaMediaDetailHistoryService(val jdbcAsyncUtils: JdbcAsyncUtils, val instaMediaDetailHistoryRepository: InstaMediaDetailHistoryRepository) : ILogging by LoggingImp<InstaMediaDetailHistoryService>() {

    fun save(instaMediaDetailHistory: InstaMediaDetailHistory): InstaMediaDetailHistory {
        return instaMediaDetailHistoryRepository.save(instaMediaDetailHistory)
    }
}
