package me.danny.instacrawlerkotlin.utils

/**
 *
 * Created by danny.ban on 2019-05-28.
 *
 * @author danny.ban
 * @since
 */
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface ILogging {
    val log: Logger
}

class LoggingImp(loggerImpl: Logger) : ILogging {
    override val log: Logger = loggerImpl

    companion object {
        inline operator fun <reified T> invoke(): LoggingImp {
            return LoggingImp(LoggerFactory.getLogger(T::class.java))
        }
    }
}