import authorization.checkAccessTokenExpiration
import file.loadToken
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import net.configuredJson
import net.helper.getSaved
import net.model.Link
import net.model.Listing
import java.io.File

suspend fun getAuthParam(): String {
    val accessToken = loadToken().accessToken
    return "Bearer $accessToken"
}

fun main(): Unit = runBlocking {
    checkAccessTokenExpiration(1800)
    val savedPosts = getSaved(getAuthParam(), "cris_null")
    val listing = savedPosts.body()
    listing?.things?.forEach { println(it) }
}