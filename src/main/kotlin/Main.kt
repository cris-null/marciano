import file.loadToken
import kotlinx.serialization.decodeFromString
import net.configuredJson
import net.model.Listing
import java.io.File

suspend fun getAuthParam(): String {
    val accessToken = loadToken().accessToken
    return "Bearer $accessToken"
}

fun main() {
    val listingResponse = File("response.json").readText()
    val decoded = configuredJson.decodeFromString<Listing>(listingResponse)
    decoded.things.forEach { println(it) }
}