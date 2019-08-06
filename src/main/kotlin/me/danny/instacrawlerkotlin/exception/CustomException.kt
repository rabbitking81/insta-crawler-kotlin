package me.danny.instacrawlerkotlin.exception

/**
 *
 * Created by danny.ban on 2019-08-06.
 *
 * @author danny.ban
 * @since
 */
class TagCrawlingException(val totalCount: Int, val endCursor: String?) : Exception("TagCrawlingException")
