package authorization

import file.loadToken
import kotlinx.coroutines.delay

suspend fun checkAccessTokenExpiration(expirationThreshold: Int) {
    println("Checking access token for expiration.")
    val accessToken = loadToken()
    if (accessToken.getSecondsTillExpiration() <= expirationThreshold) {
        println("Token under threshold! Refreshing token.")
        refreshAccessToken()
    } else
        println("No need to refresh.")
}

/**
 * Monitors the currently used access token every [checkThreshold] in milliseconds. If the number of
 * seconds till the access token expires are below of equal to [expirationThreshold] in seconds, then
 * the access token will be refreshed.
 */
suspend fun monitorAccessTokenExpiration(expirationThreshold: Int, checkThreshold: Long) {
    while (true) {
        checkAccessTokenExpiration(expirationThreshold)
        delay(checkThreshold)
    }
}