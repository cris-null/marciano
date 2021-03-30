package net

import com.beust.klaxon.FieldRenamer
import com.beust.klaxon.Json
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Formatted class for easy JSON imports with the Klaxon library.
 *
 * Contains all the information from the JSON response for an access token as fields.
 *
 * Contains utility methods to determine expiration time of the token.
 */
data class AccessToken(
    // The Json annotation for the Klaxon library allows to define what said variable
    // will look for in the JSON file.
    // Use mainly to maintain Kotlin style conventions of using camelCase, instead of
    // temporarily using snake_case to properly parse the JSON file.
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    // Unix epoch seconds
    @Json(name = "expires_in")
    val duration: Int,
    val scope: String,
    @Json(name = "refresh_token")
    val refreshToken: String,
) {

    @Json(name = "retrieved_at")
    val retrievedAt = getCurrentTimeInSeconds()

    /**
     * Calculates how many seconds remain till this token expires, using Unix Epoch seconds.
     */
    fun getSecondsTillExpiration(): Long = getExpirationDateInSeconds() - getCurrentTimeInSeconds()

    /**
     * @return The moment when this token will expire, in Unix Epoch seconds.
     */
    private fun getExpirationDateInSeconds(): Long = retrievedAt + duration

    private fun getCurrentTimeInSeconds(): Long {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val currentTimeInEpochSeconds = now.atZone(ZoneOffset.UTC).toEpochSecond()
        return currentTimeInEpochSeconds
    }

    override fun toString(): String {
        return "RefreshTokenResponse(accessToken=$accessToken, tokenType=$tokenType, duration=$duration, scope=$scope, refreshToken=$refreshToken, retrievedAt=$retrievedAt)\n" +
                "Seconds till expiration: ${getSecondsTillExpiration()}"
    }
}

/**
 * For easy saving and loading with JSON and Klaxon.
 */
object TokenResponseJsonRenamer : FieldRenamer {

    override fun fromJson(fieldName: String): String = FieldRenamer.underscoreToCamel(fieldName)

    override fun toJson(fieldName: String): String = FieldRenamer.camelToUnderscores(fieldName)

}