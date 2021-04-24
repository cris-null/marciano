package net.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

@Serializable
sealed class Thing {
    abstract val id: String
}

@Serializable
data class Comment(
    override val id: String,
//    val comment: String,
) : Thing()

@Serializable
data class Link(
    override val id: String,
//    val link: String,
) : Thing()


object ThingSerializer : JsonTransformingSerializer<List<Thing>>(ListSerializer(Thing.serializer())) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
//        val thingArray = element.jsonObject["data"]!!.jsonObject["children"]!!.jsonArray
        val thingArray = element.jsonArray
        return JsonArray(thingArray.map {
            JsonObject(
                it.jsonObject["data"]!!.jsonObject.toMutableMap().apply {
                    this["type"] = if(it.jsonObject["kind"]!!.jsonPrimitive.content == "t1") {
                        JsonPrimitive("net.model.Comment")
                    } else {
                        JsonPrimitive("net.model.Link")
                    }
                }
            )
        })
    }
}