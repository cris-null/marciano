package authorization

import Logger
import kotlinx.coroutines.delay

/**
 * Keeps track of when the current access code will expire, and requests a new fresh one when it's close
 * to expiration.
 */
object AccessTokenExpirationWatchdog {

    private val TAG = javaClass.simpleName

    fun checkAccessTokenExpiration(expirationThreshold: Int) {
        Logger.log(TAG, "Checking access token for expiration.")
        val accessToken = AccessTokenManager.getSavedToken()
        if (accessToken.getSecondsTillExpiration() <= expirationThreshold) {
            Logger.log(TAG, "Token under threshold! Refreshing token.")
            AccessTokenManager.refreshAccessToken()
        } else
            Logger.log(TAG, "No need to refresh.")
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
}