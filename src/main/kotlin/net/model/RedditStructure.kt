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

    /**
     * The time of creation in UTC epoch-second format. Note that neither of these ever have a non-zero fraction.
     */
    val createdUtc: Long
}

/** Reddit base class. */
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
    override val createdUtc: Long,

    /** The account name of the poster */
    val author: String,
    /** The raw text. Includes raw markup characters such as "*" */
    val body: String,
    /** true if this post is saved by the logged in user */
    val saved: Boolean,
    /** the net-score of the comment */
    val score: Int,
    /** subreddit of thing excluding the /r/ prefix. "pics" */
    val subreddit: String,
    /** the id of the subreddit in which the thing is located */
    val subredditId: String,
    /** to allow determining whether they have been distinguished by moderators/admins.
     * null = not distinguished. moderator = the green [M].
     * admin = the red [A].
     * special = various other special distinguishes */
    val distinguished: String


) : Votable, Created, Thing()

@Serializable
data class Link(
    override val ups: Int,
    override val downs: Int,
    override val likes: Boolean?,
    override val created: Long,
    override val createdUtc: Long,
    override val id: String,
    override val name: String,

    /** The account name of the poster. null if this is a promotional link */
    val author: String,
    /** The domain of this link.
     * Self posts will be self.<subreddit> while other examples include en.wikipedia.org and s3.amazon.com */
    val domain: String,
    /** True if the post is hidden by the logged in user. false if not logged in or not hidden. */
    val hidden: Boolean,
    /** True if this link is a self post. */
    @SerialName("is_self")
    val isSelf: Boolean,
    /** Whether the link is locked (closed to new comments) or not. */
    @SerialName("locked")
    val isLocked: Boolean,
    /** The link of this post. the permalink if this is a self-post */
    val url: String

) : Votable, Created, Thing()

object PolymorphicThingUnwrapper : JsonTransformingSerializer<List<Thing>>(ListSerializer(Thing.serializer())) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        println("transformDeserialize()")
        val things: JsonArray = element.jsonObject["data"]!!.jsonObject["children"]!!.jsonArray
//        println(things)

        return JsonArray(things.map {
            val data = it.jsonObject["data"]!!.jsonObject
            val codedType = it.jsonObject["kind"]!!
            val type = if (codedType.jsonPrimitive.content == "t1") {
                JsonPrimitive("Comment")
            } else {
                JsonPrimitive("Link")
            }



            val a = JsonObject(
                data.toMutableMap().apply { this["type"] = type }
            )

            println(a)

            return@map a
        })
    }
}