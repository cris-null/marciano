package net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

/**
 * The response received from this endpoint is a list of trophy objects.
 */
@Serializable
data class TrophyResponse(
    // Uses a custom serializer better target the wanted data.
    @Serializable(with = TrophyListSerializer::class)
    @SerialName("data")
    val trophies: List<Trophy>
)

/**
 * Each individual trophy. Note: not all elements are listed here. This model
 * can be expanded.
 */
@Serializable
data class Trophy(
    @SerialName("icon_70")
    val iconUrl: String,
    @SerialName("granted_at")
    val grantedAt: Long?,
    val name: String,
)

/**
 * The JSON response contains a lot of filler that you don't want. This custom serializer jumps straight to the
 * data of each individual trophy, collects it, and returns it as a [JsonArray].
 */
object TrophyListSerializer : JsonTransformingSerializer<List<Trophy>>(ListSerializer(Trophy.serializer())) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        // [TrophyList] sends you straight to the the "data" element, which is a JSON object
        // that contains 1 element: an array called "trophies".
        if (element is JsonObject) {
            val trophies: JsonElement = element["trophies"]!!
            // This array contains the information for every trophy the user has, but that
            // information is inside a nested "data" object, so you must extract it.
            if (trophies is JsonArray) {
                // [TrophyList] wants a List of [Trophy]
                val trophyData = mutableListOf<JsonElement>()
                trophies.forEach {
                    // The information for each [Trophy] is inside the "data" object.
                    trophyData.add(it.jsonObject["data"]!!)
                }
                // By returning a [JsonArray] you will automatically get a list of those elements, no need
                // to do anything else. The serializer already knows how to convert each element in the array
                // into a [Trophy].
                return JsonArray(trophyData.toList())
            }
        }

        // This point should only be reached if the structure of the response changes.
        return element
    }
}