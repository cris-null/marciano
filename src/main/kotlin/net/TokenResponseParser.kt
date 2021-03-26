package net

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

object TokenResponseParser {

    /**
     * Parses a string of Reddit's response to a POST request for an access token and
     * turns it into a [RefreshTokenResponse].
     */
    fun parse(responseJson: String): RefreshTokenResponse? {
        return Klaxon().parse<RefreshTokenResponse>(responseJson)
    }
}

/**
 * Formatted class for easy JSON imports with the Klaxon library.
 *
 * Contains all the information from the JSON response for an access token as fields.
 */
data class RefreshTokenResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    // Unix epoch seconds
    @Json(name = "expires_in")
    val expiresInt: Int,
    val scope: String,
    @Json(name = "refresh_token")
    val refreshToken: String,
)