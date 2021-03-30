import authorization.AccessTokenExpirationWatchdog
import authorization.AccessTokenManager
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        AccessTokenExpirationWatchdog.monitorAccessTokenExpiration(3500, 10000)
    }
}
