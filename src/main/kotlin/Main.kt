import authorization.AccessTokenExpirationWatchdog
import authorization.AccessTokenManager
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AccessTokenManager.codeFlowAuthorization()
}