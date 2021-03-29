package authorization

import java.time.LocalDateTime
import java.time.ZoneOffset

data class AuthorizationTokenPair(val accessToken: AccessToken, val refreshTokenValue: String)

/**
 * @param duration How many Unix Epoch seconds will the token last. (default seems to be 1 hour, or 3600 seconds)
 */
data class AccessToken(val value: String, private val duration: Long) {

    /**
     * Calculates how many seconds remain till this token expires, using Unix Epoch seconds.
     */
    fun getSecondsTillExpiration(): Long = getExpirationTime() - getCurrentTimeInEpochSeconds()

    /**
     * @return The moment when this token will expire, in Unix Epoch seconds.
     */
    fun getExpirationTime(): Long = getCurrentTimeInEpochSeconds() + duration
    
    private fun getCurrentTimeInEpochSeconds(): Long {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val currentTimeInEpochSeconds = now.atZone(ZoneOffset.UTC).toEpochSecond()
        return currentTimeInEpochSeconds
    }
}