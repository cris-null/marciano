package net

import constant.RegisteredAppInformation

/**
 * Access tokens are returned by Reddit inside a JSON
 */
object AuthorizationResponseRetriever {

    /**
     * Indicates that you're using the "standard" code based flow. Used when requesting a new
     * access token after being granted authorization by the user.
     */
    private const val AUTHORIZATION_GRANT_TYPE = "authorization_code"

    /**
     * Indicates that you're requesting a new access token using a refresh token.
     */
    private const val REFRESH_GRANT_TYPE = "refresh_token"

    /**
     * After the user authorizes your application, if you didn't get an error and the state value checks out,
     * you may then make a POST request with [code] (a one-time use code that may be exchanged for a bearer token),
     * to retrieve an access token.
     *
     * This function makes the POST request needed to get that access token (given in the form of a JSON string).
     *
     * @param code The code retrieved from the redirect URI after a successful authorization.
     * @return The JSON response returned by Reddit after receiving an authorization request.
     */
    fun getAuthorizationResponse(code: String): String {
        val parameters = "grant_type=$AUTHORIZATION_GRANT_TYPE&code=$code&redirect_uri=${RegisteredAppInformation.REDIRECT_URI}"
        val requestMaker = AuthPostRequestMaker(parameters)
        return requestMaker.requestAccessTokenFromReddit()
    }

    /**
     * Access tokens expire after 1 hour. To get a new one use your refresh token (it is obtained by requesting
     * an access token with duration=permanent).
     *
     * This function makes the POST request needed to get that new access token (given in the form of a JSON string).
     *
     * @return The JSON response returned by Reddit after receiving a refresh request.
     */
    fun getRefreshTokenResponse(refreshToken: String): String {
        val parameters = "grant_type=$REFRESH_GRANT_TYPE&refresh_token=$refreshToken"
        val requestMaker = AuthPostRequestMaker(parameters)
        return requestMaker.requestAccessTokenFromReddit()
    }
}