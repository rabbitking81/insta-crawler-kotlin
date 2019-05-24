package me.danny.instacrawlerkotlin

import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerResponse

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
fun ServerResponse.BodyBuilder.json(): ServerResponse.BodyBuilder = contentType(APPLICATION_JSON_UTF8)
