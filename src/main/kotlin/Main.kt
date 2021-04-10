import authorization.AccessTokenExpirationWatchdog
import data.net.helper.IdentityHelper
import file.FileTokenManager
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AccessTokenExpirationWatchdog.checkAccessTokenExpiration(1800)

    val accessToken = FileTokenManager.getAccessTokenFromFile().accessToken
    val auth = "Bearer $accessToken"
    val response = IdentityHelper.getTrophies(auth)
    if (response.isSuccessful) {
        val trophies = response.body()
        println(trophies)
    }
}