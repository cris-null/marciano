import authorization.checkAccessTokenExpiration
import file.loadToken
import kotlinx.coroutines.runBlocking
import net.helper.getTrophies

fun main() = runBlocking {
    checkAccessTokenExpiration(1800)

    val accessToken: String = loadToken().accessToken
    val authorizationParameter = "Bearer $accessToken"
    val response = getTrophies(authorizationParameter)
    if (response.isSuccessful) {
        val trophies = response.body()
        println(trophies)
    }
}