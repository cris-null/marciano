package net

import com.beust.klaxon.Klaxon

object AccessTokenResponseParser {

    /**
     * Parses a string of Reddit's response to a POST request, for an access token, and
     * turns it into a [AccessToken].
     */
    fun parse(responseJson: String): AccessToken? {
        return Klaxon().parse<AccessToken>(responseJson)
    }
}