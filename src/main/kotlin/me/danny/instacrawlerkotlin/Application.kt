package me.danny.instacrawlerkotlin

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplicationBuilder()
        .sources(Application::class.java)
        .web(WebApplicationType.REACTIVE)
        .run(*args)
}
