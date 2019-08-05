package me.danny.instacrawlerkotlin.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * Created by rabbitking81 on 2019-08-04.
 *
 * @author rabbitking81
 * @since
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class InstaUser(
    @JsonProperty("pk")
    val pk: Long,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("full_name")
    val fullName: String,

    @JsonProperty("is_private")
    val isPrivate: Boolean,

    @JsonProperty("media_count")
    val mediaCount: Int,

    @JsonProperty("follower_count")
    val followerCount: Int,

    @JsonProperty("following_count")
    val followingCount: Int,

    @JsonProperty("public_phone_country_code")
    val publicPhoneCountryCode: String?
)