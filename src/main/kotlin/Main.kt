import authorization.AccessTokenExpirationWatchdog
import authorization.AccessTokenManager
import data.net.ServiceBuilder
import data.net.api.IdentityService
import data.net.helper.IdentityHelper
import file.FileTokenManager
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AccessTokenExpirationWatchdog.checkAccessTokenExpiration(1000)

    val accessToken = FileTokenManager.getAccessTokenFromFile().accessToken
    val authString = "Bearer $accessToken"
    val response = IdentityHelper.getIdentity(authString)
    if (response.isSuccessful) {
        println("success")
        println("body:\n${response.body()}")
    } else
        println("failed")
}