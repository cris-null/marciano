package net.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

/**
 * Used to paginate content that is too long to display in one go.
 *
 * Add the query argument before or after with the value given to get the previous or next page.
 * This is usually used in conjunction with a count argument.
 */
@Serializable(with = ListingSerializer::class)
data class Listing(
    val after: String?,
    val before: String?,
    @Serializable(with = PolymorphicThingUnwrapper::class)
    val things: List<Thing>?
)

object ListingSerializer: KSerializer<Listing> {

    override fun deserialize(decoder: Decoder): Listing {
        require(decoder is JsonDecoder)

        val everything = decoder.decodeJsonElement()
        val data = everything.jsonObject["data"]!!
        val after = data.jsonObject["after"]!!.jsonPrimitive.contentOrNull
        val before = data.jsonObject["before"]!!.jsonPrimitive.contentOrNull

        val things = mutableListOf<Thing>()
        val thingJsonArray = data.jsonObject["children"]!!.jsonArray


//        thingJsonArray.forEach { element ->
//
//            val kind = element.jsonObject["kind"]!!
//
//        }



        return Listing(after, before ,null)
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Listing") {
        element<String>("after")
        element<String>("before")
        element("things", listSerialDescriptor<Thing>())
    }

    override fun serialize(encoder: Encoder, value: Listing) {
        require(encoder is JsonEncoder)
        encoder.encodeJsonElement(JsonObject(mapOf("after" to JsonPrimitive(value.after))))
    }

}