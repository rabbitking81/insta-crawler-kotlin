package me.danny.instacrawlerkotlin.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.danny.instacrawlerkotlin.model.entity.InstaMedia
import java.sql.Timestamp

/**
 *
 * Created by rabbitking81 on 2019-05-26.
 *
 * @author rabbitking81
 * @since
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class EdgeOwnerToTimelineMedia(
    val count: Long,

    @JsonProperty("page_info")
    val pageInfo: PageInfo,

    @JsonProperty("edges")
    val edges: List<Edge>
) {
    data class PageInfo(
        @JsonProperty("has_next_page")
        val hasNextPage: Boolean,

        @JsonProperty("end_cursor")
        val endCursor: String?
    )

    data class Edge(
        @JsonProperty("node")
        val node: EdgeItem
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EdgeItem(
        @JsonProperty("id")
        val id: String,

        @JsonProperty("__typename")
        val __typename: String,

        @JsonProperty("shortcode")
        val shortcode: String,

        @JsonProperty("edge_media_to_caption")
        val edgeMediaToCaption: EdgeMediaToCaption,

        @JsonProperty("edge_media_to_comment")
        val edgeMediaToComment: EdgeMediaToComment,

        @JsonProperty("edge_media_preview_like")
        val edgeMediaPreviewLike: EdgeMediaPreviewLike,

        @JsonProperty("is_video")
        val isVideo: Boolean,

        @JsonProperty("display_url")
        val displayUrl: String,

        @JsonProperty("taken_at_timestamp")
        val takenAtTimestamp: Timestamp
    ) {
        fun toInstaMedia(userId: Long?): InstaMedia {
            return InstaMedia(shortCode = this.shortcode, instaMediaId = this.id, imageUrl = this.displayUrl, instaType = this.__typename, userId = userId, instaCreatedDate = this.takenAtTimestamp)
        }

        data class EdgeMediaToCaption(
            @JsonProperty("edges")
            val edges: List<SubEdge>
        ) {
            data class SubEdge(
                @JsonProperty("node")
                val node: SubNode
            ) {
                data class SubNode(
                    @JsonProperty("text")
                    val text: String
                )
            }
        }

        data class EdgeMediaToComment(
            @JsonProperty("count")
            val count: Long
        )

        data class EdgeMediaPreviewLike(
            @JsonProperty("count")
            val count: Long
        )
    }
}