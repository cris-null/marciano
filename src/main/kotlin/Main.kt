import authorization.checkAccessTokenExpiration
import file.loadToken
import kotlinx.coroutines.runBlocking
import net.helper.fetchHotPics
import java.io.File

fun main() = runBlocking {
    checkAccessTokenExpiration(1800)
}

suspend fun getAuthParam(): String {
    val accessToken = loadToken().accessToken
    return "Bearer $accessToken"
}