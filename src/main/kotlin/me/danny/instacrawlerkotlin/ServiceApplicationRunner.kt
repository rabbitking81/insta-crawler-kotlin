package me.danny.instacrawlerkotlin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.danny.instacrawlerkotlin.model.entity.InstaMediaDetailHistory
import me.danny.instacrawlerkotlin.utils.ILogging
import me.danny.instacrawlerkotlin.utils.LoggingImp
import me.danny.instacrawlerkotlin.utils.getInstagramPostId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import org.springframework.web.servlet.DispatcherServlet
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 *
 * Created by danny.ban on 2019-05-30.
 *
 * @author danny.ban
 * @since
 */
@Component
class ServiceApplicationRunner : ApplicationRunner, ILogging by LoggingImp<ServiceApplicationRunner>() {
    @Autowired
    private val servlet: DispatcherServlet? = null

    @Autowired
    lateinit var environment: Environment

    private val instaLogger = LoggerFactory.getLogger("insta")

    val objectMapper = jacksonObjectMapper()

    override fun run(args: ApplicationArguments?) {
        val utcZoned = ZonedDateTime.now(ZoneId.of("UTC"))
        val koZoneId = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
        val utcLocalTime = utcZoned.toLocalDateTime()
        val isProduction = environment.activeProfiles.contains("prod")

        val limitDateTime = "2018-08-31 00:00:00.0" // 형식을 지켜야 함
        val limitDateTimeStamp = java.sql.Timestamp.valueOf(limitDateTime)
        val startDateTime = "2018-09-01 00:00:00.0" // 형식을 지켜야 함
        val endDateTime = "2018-11-01 00:00:00.0" // 형식을 지켜야 함
        val startDateTimeStamp = java.sql.Timestamp.valueOf(startDateTime)
        val endDateTimeStamp = java.sql.Timestamp.valueOf(endDateTime)

        log.run {
            info("===============================================")
            info("START APPLICATION")
            info("UTC TIME : $utcLocalTime" + " = ${Timestamp.valueOf(utcLocalTime).time}")
            info("KST TIME : ${koZoneId.toLocalDateTime()}")
            info("----" + getInstagramPostId("2061032530567649687"))
            info("----" + getInstagramPostId("2062506575096037653_2062506559954640850"))
            info("start" + startDateTimeStamp.time)
            info("end" + endDateTimeStamp.time)
            info("===============================================")
        }

        servlet?.setThrowExceptionIfNoHandlerFound(true)
    }
}