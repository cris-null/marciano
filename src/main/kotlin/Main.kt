import file.loadToken
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import net.configuredJson
import net.model.Listing
import net.model.PolymorphicThingUnwrapper
import java.io.File

suspend fun getAuthParam(): String {
    val accessToken = loadToken().accessToken
    return "Bearer $accessToken"
}

fun main() = runBlocking {
    val listingResponse = File("response.json").readText()
//    val decoded = configuredJson.decodeFromString<Listing>(listingResponse)
//    println(decoded)

    val test = Json.decodeFromString(PolymorphicThingUnwrapper, listingResponse)
    println(test)
}