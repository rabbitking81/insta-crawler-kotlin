package me.danny.instacrawlerkotlin.utils

/**
 *
 * Created by danny.ban on 2019-08-14.
 *
 * @author danny.ban
 * @since
 */

fun getInstagramPostId(mediaId: String): String {
    var postId = ""
    try {
        var id =
            if(mediaId.contains('_')) {
                java.lang.Long.parseLong(mediaId.substring(0, mediaId.indexOf('_')))
            } else {
                java.lang.Long.parseLong(mediaId)
            }

        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"

        while (id > 0) {
            val remainder = id % 64
            id = (id - remainder) / 64
            postId = alphabet[remainder.toInt()] + postId
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return postId
}