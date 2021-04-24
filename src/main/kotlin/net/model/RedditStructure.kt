package net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*


/** Describes the data structure of objects returned when using the Reddit JSON API. */

interface Votable {
    /* The number of upvotes. Includes own. */
    val ups: Int

    /* The number of downvotes. Includes own. */
    val downs: Int

    /**
     * True if thing is liked by the user, false if thing is disliked,
     * null if the user has not voted or you are not logged in.
     */
    val likes: Boolean?
}

interface Created {
    /**
     * The time of creation in local epoch-second format. ex: 1331042771.0
     *
     * Reddit says that the type is a [Long] but since it ends in ".0" you
     * must store it as a double. However, in some tests, the response did not
     * have a double, so maybe Long is fine? Todo: test if it's fine to use Long instead of Double.
     */
    val created: Long
}

@Serializable
sealed class Thing {
    /** This item's identifier, e.g. "8xwlg" */
    abstract val id: String

    /** Fullname of comment, e.g. "t1_c3v7f8u" */
    abstract val name: String
}

@Serializable
data class Comment(
    override val id: String,
    override val name: String,
    override val ups: Int,
    override val downs: Int,
    override val likes: Boolean?,
    override val created: Long,

    /** Who approved this comment. null if nobody or you are not a mod */
    @SerialName("approved_by")
    val approvedBy: String?,

    /** The account name of the poster */
    val author: String,

    /** The CSS class of the author's flair. subreddit specific */
    @SerialName("author_flair_css_class")
    val authorFlairCssClass: String?,

    /** The text of the author's flair. subreddit specific */
    @SerialName("author_flair_text")
    val authorFlairText: String?,

    /** Who removed this comment. Null if nobody or you are not a mod */
    @SerialName("banned_by")
    val bannedBy: String?,

    /**
     * The raw text.
     *
     * This is the unformatted text which includes the raw markup characters such as ** for bold.
     *
     * <, >, and & are escaped. */
    val body: String,

    /**
     * The formatted HTML text as displayed on reddit.
     *
     * For example, text that is emphasised by * will now have <em> tags wrapping it.
     * Additionally, bullets and numbered lists will now be in HTML list format.
     *
     * NOTE: The HTML string will be escaped. You must unescape to get the raw HTML.
     */
    @SerialName("body_html")
    val bodyHtml: String,

    /** The number of times this comment received reddit gold */
    @SerialName("gilded")
    val gildedCount: Int,

    /** ID of the link this comment is in */
    @SerialName("link_id")
    val linkId: String,

    /** How many times this comment has been reported, null if not a mod */
    @SerialName("num_reports")
    val reportCount: Int?,

    /** ID of the thing this comment is a reply to, either the link or a comment in it */
    @SerialName("parent_id")
    val parentId: String,

    /** True if this post is saved by the logged in user */
    val saved: Boolean,

    /** The net-score of the comment */
    val score: Int,

    /** Whether the comment's score is currently hidden */
    @SerialName("score_hidden")
    val isScoreHidden: Boolean,

    /** Subreddit of the thing excluding the /r/ prefix. "pics" */
    val subreddit: String,

    /** The ID of the subreddit in which the thing is located */
    @SerialName("subreddit_id")
    val subredditId: String,

    /**
     * To allow determining whether they have been distinguished by moderators/admins.
     *
     * null = not distinguished.
     *
     * moderator = the green [M].
     *
     * admin = the red [A].
     *
     * special = various other special distinguishes http://redd.it/19ak1b
     */
    val distinguished: String?

) : Thing(), Votable, Created

@Serializable
data class Link(
    override val id: String,
    override val ups: Int,
    override val downs: Int,
    override val likes: Boolean?,
    override val created: Long,
    override val name: String,


    ) : Thing(), Votable, Created


/**
 * A listing response contains many [Thing], which can be [Comment] or [Link]. This object takes a [JsonArray]
 * of [Thing] and returns a [List] of [Thing], properly classified into [Comment] or [Link].
 */
object PolymorphicThingUnwrapper : JsonTransformingSerializer<List<Thing>>(ListSerializer(Thing.serializer())) {

    /** @param element A [JsonArray] of [Thing] */
    override fun transformDeserialize(element: JsonElement): JsonElement {
        require(element is JsonArray)
        val thingArray = element.jsonArray

        return JsonArray(thingArray.map {
            JsonObject(
                it.jsonObject["data"]!!.jsonObject.toMutableMap().apply {
                    // Add a "type" to the JSON object to preserve the class discriminator.
                    // This allows seamless polymorphism.
                    // Reddit classifies it's data structures according to type by using a prefix.
                    // t1 means that the [Thing] is a [Comment], t2 means [Link]
                    this["type"] =
                        if (it.jsonObject["kind"]!!.jsonPrimitive.content == "t1") {
                            JsonPrimitive("net.model.Comment")
                        } else {
                            JsonPrimitive("net.model.Link")
                        }
                }
            )
        })
    }
}