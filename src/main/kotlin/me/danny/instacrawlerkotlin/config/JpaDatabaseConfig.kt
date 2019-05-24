package me.danny.instacrawlerkotlin.config

import me.danny.instacrawlerkotlin.config.properties.DataSourceHikariProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Executors

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
@Configuration
@EnableConfigurationProperties(DataSourceHikariProperties::class)
//@EnableJpaRepositories(basePackages = [""])
class JpaDatabaseConfig {

    @Autowired
    lateinit var dataSourceHikariProperties: DataSourceHikariProperties

    @Bean
    fun jdbcScheduler(): Scheduler {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(dataSourceHikariProperties.maximumPoolSize.toInt()))
    }
}