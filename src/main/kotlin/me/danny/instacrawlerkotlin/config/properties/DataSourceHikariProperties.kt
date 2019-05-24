package me.danny.instacrawlerkotlin.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.datasource.hikari")
class DataSourceHikariProperties {
    lateinit var maximumPoolSize: String
}