package net

import constant.RegisteredAppInformation

object AccessTokenRetriever {

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
     * @param code The code retrieved from the redirect URI after a successful authorization.
     * @return A String representing a JSON response with the access token, among other things.
     */
    fun getAccessTokenResponse(code: String): String {
        val parameters = "grant_type=$AUTHORIZATION_GRANT_TYPE&code=$code&redirect_uri=${RegisteredAppInformation.REDIRECT_URI}"
        val requestMaker = AuthPostRequestMaker(parameters)
        return requestMaker.requestAccessTokenFromReddit()
    }

    /**
     * Access tokens expire after 1 hour. To get a new one use your refresh token (it is obtained by requesting
     * an access token with duration=permanent.
     */
    fun getRefreshedAccessTokenResponse(refreshToken: String): String {
        val parameters = "grant_type=$REFRESH_GRANT_TYPE&refresh_token=$refreshToken"
        val requestMaker = AuthPostRequestMaker(parameters)
        return requestMaker.requestAccessTokenFromReddit()
    }

}