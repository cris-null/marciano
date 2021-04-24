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


    ) : Thing(), Votable, Created

@Serializable
data class Link(
    override val id: String,
    override val ups: Int,
    override val downs: Int,
    override val likes: Boolean?,
    override val created: Long,
    override val createdUtc: Long,
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