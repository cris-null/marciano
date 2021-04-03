package authorization

import java.util.*

/**
 * The RFC defines HTTP Basic Auth [here](https://tools.ietf.org/html/rfc2617).
 *
 * For our purposes, it looks something like:
 *
 * Authorization: Basic encoded_string
 *
 * It has a "user" (which for Reddit is your client ID), and a "password" (your client secret, unless
 * you are making a "installed app", in which case it's an empty string).
 *
 * In practice, you will use this object to obtain the value that goes with the HTTP header
 * "Authorization" when making a request for an access token for the first time.
 */
object HttpBasicAuthFormatter {

    /**
     * @return The proper value to pair with an "Authorization" header when making a
     * request for an access token.
     */
    fun getBasicAuth(clientId: String, clientSecret: String): String {
        val credentials = "$clientId:$clientSecret"
        return "Basic " + String(Base64.getEncoder().encode(credentials.toByteArray()))
    }
}