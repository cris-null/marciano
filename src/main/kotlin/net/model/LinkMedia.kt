package net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
sealed class LinkMedia

@Serializable
data class RedditVideo(
    @SerialName("fallback_url")
    val fallbackUrl: String,
    val height: Int,
    val width: Int,
    val duration: Int,
    @SerialName("is_gif")
    val isGif: Boolean
) : LinkMedia()

@Serializable
data class YoutubeVideo(
    /** The original title of the video as seen on YouTube itself */
    @SerialName("title")
    val originalTitle: String,
    val height: Int,
    val width: Int,
    /** The name of the channel that made the video */
    @SerialName("author_name")
    val originalAuthor: String,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    @SerialName("thumbnail_height")
    val thumbnailHeight: Int,
    /** A link to the channel that made the video */
    @SerialName("author_url")
    val originalAuthorUrl: String,
) : LinkMedia()

object LinkMediaSerializer: JsonTransformingSerializer<LinkMedia>(LinkMedia.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        // Check the documentation for what an example of a response of this type looks like.
        // Currently only Youtube and Reddit videos are supported.

        val fullObject = element.jsonObject

        if ("type" in fullObject) {
            // Reddit video do not have a "type" key so this is a Youtube video
            if ("youtube.com" == fullObject["type"]!!.jsonPrimitive.content) {
                // Transforming the object into a mutable map so to add a "type" key
                // so that polymorphism is enabled
                val data = fullObject["oembed"]!!.jsonObject.toMutableMap()
                data["type"] = JsonPrimitive("net.model.YoutubeVideo")
                return JsonObject(data)
            }
        } else {
            // We are in a Reddit video
            val data = fullObject["reddit_video"]!!.jsonObject.toMutableMap()
            data["type"] = JsonPrimitive("net.model.RedditVideo")
            return JsonObject(data)
        }

        // If you reach this point, the probably the media attached to the [Link]
        // did not come from YouTube.
        throw Exception("error while parsing LinkMedia")
    }
}