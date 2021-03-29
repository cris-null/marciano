package authorization

import java.time.LocalDateTime
import java.time.ZoneOffset

data class TokenPair(val accessToken: AccessToken, val refreshToken: String)

/**
 * @param duration How many Unix Epoch seconds will the token last. (default seems to be 1 hour, or 3600 seconds)
 */
data class AccessToken(val accessToken: String, private val duration: Long) {

    fun secondsTillExpiration(): Long {
        // LocalDataTime to epoch seconds
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val currentTimeInEpochSeconds = now.atZone(ZoneOffset.UTC).toEpochSecond()
        return determineExpirationTime() - currentTimeInEpochSeconds
    }

    private fun determineExpirationTime(): Long {
        // LocalDataTime to epoch seconds
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val currentTimeInEpochSeconds = now.atZone(ZoneOffset.UTC).toEpochSecond()
        return currentTimeInEpochSeconds + duration
    }
}