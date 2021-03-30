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
data class RefreshTokenResponse(
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
    val refreshToken: String
) {

    // Klaxon will not save private properties to a JSON unless annotated
    @Json(ignored = false)
    private val retrievedAt = getCurrentTimeInEpochSeconds()

    /**
     * Calculates how many seconds remain till this token expires, using Unix Epoch seconds.
     */
    fun getSecondsTillExpiration(): Long = getExpirationTime() - getCurrentTimeInEpochSeconds()

    /**
     * @return The moment when this token will expire, in Unix Epoch seconds.
     */
    private fun getExpirationTime(): Long = retrievedAt + duration

    private fun getCurrentTimeInEpochSeconds(): Long {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val currentTimeInEpochSeconds = now.atZone(ZoneOffset.UTC).toEpochSecond()
        return currentTimeInEpochSeconds
    }
}

/**
 * For easy saving and loading with JSON and Klaxon.
 */
object RefreshTokenResponseJsonRenamer : FieldRenamer {

    override fun fromJson(fieldName: String): String = FieldRenamer.underscoreToCamel(fieldName)

    override fun toJson(fieldName: String): String = FieldRenamer.camelToUnderscores(fieldName)

}