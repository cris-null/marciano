package net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Data class that represent the response send by Reddit when you successfully
 * authorize and receive an authorization token.
 *
 * Note that depending on whether you requested a permanent authorization, there might
 * or might not be a refresh token included in the response.
 *
 * Note that the JSON parsing library Moshi has support for Kotlin via code-gen which allows
 * nullable types to be declared in a JSON model class. This means that an error will be thrown
 * if a non-nullable property is in the class but not in the JSON. However, if a nullable property
 * is in the class and not in the JSON, then the value will just be set to null.
 *
 * This means that this single can handle both kinds of authorization response, those that contain
 * a refresh token, and those that don't.
 */
@Serializable
class AccessToken(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val duration: Int,
    @SerialName("refresh_token")
    val refreshToken: String? = null,
    val scope: String
) {

    @SerialName("retrieved_at")
    val retrievedAt = getCurrentTimestamp()

    /** Returns how many seconds remain till this token expires */
    fun getSecondsTillExpiration(): Long = getExpirationTimestamp() - getCurrentTimestamp()

    /** Returns the moment when this token will expire in seconds since the epoch. */
    private fun getExpirationTimestamp(): Long = retrievedAt + duration

    /** Returns the current number of seconds since the epoch. */
    private fun getCurrentTimestamp(): Long {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        return now.atZone(ZoneOffset.UTC).toEpochSecond()
    }

    override fun toString(): String {
        return "AccessToken(accessToken=$accessToken, tokenType=$tokenType, " +
                "duration=$duration, refreshToken=$refreshToken, " +
                "scope=$scope, retrievedAt=$retrievedAt"
    }
}