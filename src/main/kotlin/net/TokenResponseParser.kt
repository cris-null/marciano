package net

import com.beust.klaxon.Klaxon

object TokenResponseParser {

    /**
     * Parses a string of Reddit's response to a POST request, for an access token, and
     * turns it into a [RefreshTokenResponse].
     */
    fun parse(responseJson: String): RefreshTokenResponse? {
        return Klaxon().parse<RefreshTokenResponse>(responseJson)
    }
}