package net.model

import getAuthParam
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import net.configuredJson
import net.helper.fetchSaved

/**
 * Used to paginate content that is too long to display in one go.
 */
@Serializable(with = ListingSerializer::class)
data class Listing(
    /** Indicates the fullname of an item in the listing to use as the anchor point of the slice */
    @SerialName("after")
    private val next: String?,
    /** Indicates the fullname of an item in the listing to use as the anchor point of the slice */
    private val before: String?,

    val posts: List<Post>
) {
    /**
     * Each [Listing] has a limited number of [Post] (1-100, 25 is the default).
     * If you want more [Post] then you need to get the "next" listing.
     *
     * @return The next [Listing]. Null if it's the last one in the "sequence".
     * There's a hard limit of 1000 for most [Listing].
     */
    suspend fun fetchNext(): Listing? {
        return if (next == null)
            return null
        else
            fetchSaved(getAuthParam(), "cris_null", next).body()
    }

    /**
     * Using [currentPage] as a starting point, continuously fetches the next [Listing]
     * until there are no more to retrieve, adding them to [listings].
     */
    suspend fun collectAll(currentPage: Listing, listings: MutableList<Listing>) {
        listings.add(currentPage)

        if (currentPage.next == null) {
            // There are no more pages to retrieve, end recursion.
            return
        } else {
            // There's at least one or more page to retrieve, fetch them
            // recursively.
            val nextPage = currentPage.fetchNext()
            requireNotNull(nextPage)
            collectAll(nextPage, listings)
        }
    }
}

object ListingSerializer : KSerializer<Listing> {

    override fun deserialize(decoder: Decoder): Listing {
        require(decoder is JsonDecoder)

        val everything = decoder.decodeJsonElement()
        val data = everything.jsonObject["data"]!!
        val after = data.jsonObject["after"]!!.jsonPrimitive.contentOrNull
        val before = data.jsonObject["before"]!!.jsonPrimitive.contentOrNull

        val thingJsonArray = data.jsonObject["children"]!!.jsonArray
        val things = configuredJson.decodeFromJsonElement(PolymorphicThingUnwrapper, thingJsonArray)

        return Listing(after, before, things)
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Listing") {
        element<String>("next")
        element<String>("before")
        element("posts", listSerialDescriptor<Post>())
    }

    override fun serialize(encoder: Encoder, value: Listing) {
        throw NotImplementedError()
//        require(encoder is JsonEncoder)
//        encoder.encodeJsonElement(JsonObject(mapOf("after" to JsonPrimitive(value.after))))
    }

}