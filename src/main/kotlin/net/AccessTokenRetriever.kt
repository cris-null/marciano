package net

import constant.RegisteredAppInformation

/**
 * Once authorized by the user, you may request an access token from Reddit.
 *
 * Access tokens are returned by Reddit inside a JSON string.
 */
object AccessTokenRetriever {

    /**
     * Indicates that you're using the "standard" code based flow. Used when requesting a new
     * access token after being granted authorization by the user.
     */
    private const val GET_NEW_TOKEN_GRANT_TYPE = "authorization_code"

    /**
     * Indicates that you're requesting a new access token using a refresh token.
     */
    private const val REFRESH_OLD_TOKEN_GRANT_TYPE = "refresh_token"

    /**
     * After the user authorizes your application, if you didn't get an error and the state value checks out,
     * you may then make a POST request with [exchangeCode] (a one-time use code that may be exchanged for a bearer token),
     * to retrieve an access token.
     *
     * This function makes the POST request needed to get that access token (given in the form of a JSON string).
     *
     * @param exchangeCode The code retrieved from the redirect URI after a successful authorization, which you
     * can exchange for an access token.
     * @return The JSON response returned by Reddit after receiving an authorization request.
     */
    fun getNewAccessToken(exchangeCode: String): String {
        val parameters =
            "grant_type=$GET_NEW_TOKEN_GRANT_TYPE&code=$exchangeCode&redirect_uri=${RegisteredAppInformation.REDIRECT_URI}"
        val requester = AccessTokenPostRequester(parameters)
        return requester.requestAccessTokenFromReddit()
    }

    /**
     * Even if the user grants access to your application, the access token obtained from Reddit expires after 1 hour.
     * In addition to an access token, if when you originally requested authorization set the duration to "permanent", then
     * Reddit will have given you a refresh token to request a brand new refresh token without making the user give permission again.
     *
     * This function makes the POST request needed to get that new access token (given in the form of a JSON string), using your old
     * refresh token.
     *
     * @return The JSON response returned by Reddit after receiving a refresh request. It will contain the new access token (which will
     * again expire in 1 hour), and a new refresh token for you to refresh in the future.
     */
    fun getRenewedAccessToken(currentRefreshToken: String): String {
        val parameters = "grant_type=$REFRESH_OLD_TOKEN_GRANT_TYPE&refresh_token=$currentRefreshToken"
        val requester = AccessTokenPostRequester(parameters)
        return requester.requestAccessTokenFromReddit()
    }
}