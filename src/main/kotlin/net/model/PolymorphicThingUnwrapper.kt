package net.model

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

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