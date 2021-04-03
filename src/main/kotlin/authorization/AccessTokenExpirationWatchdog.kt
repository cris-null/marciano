package authorization

import file.FileTokenManager
import kotlinx.coroutines.delay

/**
 * Keeps track of when the current access code will expire, and requests a new fresh one when it's close
 * to expiration.
 */
object AccessTokenExpirationWatchdog {

    /**
     * Monitors the currently used access token every [checkThreshold] in milliseconds. If the number of
     * seconds till the access token expires are below of equal to [refreshThreshold] in seconds, then
     * the access token will be refreshed.
     */
//    suspend fun monitorAccessTokenExpiration(refreshThreshold: Int, checkThreshold: Long) {
//        while (true) {
//            println("\nAccessTokenWatchdog on duty!")
//            val accessToken = FileTokenManager.getAccessTokenFromFile()
//
//            println("Checking if the access token should be refreshed...")
//            if (accessToken.getSecondsTillExpiration() <= refreshThreshold) {
//                println("It should! Refreshing access token!")
//                AccessTokenManager.refreshCurrentAccessToken()
//            } else
//                println("It should not. Taking a nap...")
//
//            delay(checkThreshold)
//        }
//    }
}