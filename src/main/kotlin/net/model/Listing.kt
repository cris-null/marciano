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
 *
 * Add the query argument before or after with the value given to get the previous or next page.
 * This is usually used in conjunction with a count argument.
 */
@Serializable(with = ListingSerializer::class)
data class Listing(
    @SerialName("after")
    val next: String?,
    val before: String?,
    val things: List<Thing>
) {
    suspend fun next(): Listing? {
        return if (next == null)
            return null
        else
            fetchSaved(getAuthParam(), "cris_null", next).body()
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
        element("things", listSerialDescriptor<Thing>())
    }

    override fun serialize(encoder: Encoder, value: Listing) {
        throw NotImplementedError()
//        require(encoder is JsonEncoder)
//        encoder.encodeJsonElement(JsonObject(mapOf("after" to JsonPrimitive(value.after))))
    }

}