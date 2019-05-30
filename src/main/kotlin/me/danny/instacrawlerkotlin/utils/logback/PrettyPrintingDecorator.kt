package me.danny.instacrawlerkotlin.utils.logback

import com.fasterxml.jackson.core.JsonGenerator
import net.logstash.logback.decorate.JsonGeneratorDecorator

/**
 *
 * Created by danny.ban on 2019-05-30.
 *
 * @author danny.ban
 * @since
 */
class PrettyPrintingDecorator : JsonGeneratorDecorator {
    override fun decorate(generator: JsonGenerator): JsonGenerator {

        return generator.useDefaultPrettyPrinter()
    }
}