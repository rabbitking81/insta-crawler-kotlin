package me.danny.instacrawlerkotlin.utils.logback

import com.fasterxml.jackson.databind.MappingJsonFactory
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import net.logstash.logback.decorate.JsonFactoryDecorator


/**
 *
 * Created by danny.ban on 2019-05-30.
 *
 * @author danny.ban
 * @since
 */
class ISO8601DateDecorator : JsonFactoryDecorator {

    override fun decorate(factory: MappingJsonFactory): MappingJsonFactory {

        val codec = factory.codec
        codec.dateFormat = ISO8601DateFormat()

        return factory
    }
}