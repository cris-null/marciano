package authorization

import Logger
import constant.RegisteredAppInformation
import data.api.RetrofitBuilder
import data.model.AccessToken
import file.FileSecretReader
import file.FileTokenManager
import net.RedirectUriResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object AccessTokenManager {

    /** For request that do not require a token */
    private const val BASE_URL = "https://www.reddit.com"

    /**
     * grant_type that indicates that you're using the "standard" code based flow.
     * Used when requesting a new access token after being granted authorization by the user.
     */
    private const val GET_NEW_TOKEN = "authorization_code"
    /** grant_type that indicates you're requesting a new access token using a refresh token */
    private const val GET_REFRESHED_TOKEN = "refresh_token"

    private val TAG = javaClass.simpleName

    fun getNewAccessToken() {
        val redirectUriResult = UserAuthorizationRequester.request()
        check(redirectUriResult is RedirectUriResult.Success) {
            Logger.log(TAG, "Bad redirect URI. " +
                    "Message = ${(redirectUriResult as RedirectUriResult.Error).message}")
        }

        val httpBasicAuth = getHttpBasicAuth()
        val requestParameters = getNewRequestParameters(redirectUriResult)
        val accessToken = requestAccessToken(httpBasicAuth, requestParameters)
        checkNotNull(accessToken)
        FileTokenManager.saveAccessTokenToFile(accessToken)
    }


    /** Needed for the "Authorization" header of the request. */
    private fun getHttpBasicAuth(): String {
        val clientId: String = RegisteredAppInformation.CLIENT_ID
        val clientSecret: String = FileSecretReader.getClientSecret()
        return HttpBasicAuthFormatter.getBasicAuth(clientId, clientSecret)
    }

    /**
     * Gets the specific parameters required for the POST request's body that asks
     * for a new access token.
     */
    private fun getNewRequestParameters(redirectUriResult: RedirectUriResult.Success): RequestBody {
        val exchangeCode: String = redirectUriResult.code
        val redirectUri: String = RegisteredAppInformation.REDIRECT_URI
        // Set the parameters Reddit requires in the POST request's body
        val parameters = "grant_type=$GET_NEW_TOKEN&code=$exchangeCode&redirect_uri=$redirectUri"
        // The parameters won't work if send as a plain string, they need to be converted.
        return parameters.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    /**
     * Makes a POST request to Reddit asking for an access token. This function can be used both for
     * requesting a new access token, or refreshing an old one, as the only thing different between them
     * are the POST body [parameters]
     * @param httpBasicAuth Needed for the "Authorization" header of the request. See [HttpBasicAuthFormatter].
     * @param parameters See [getNewRequestParameters]
     * @return An [AccessToken] if the response is successful, null otherwise.
     */
    private fun requestAccessToken(httpBasicAuth: String, parameters: RequestBody): AccessToken? {
        val retrofit = RetrofitBuilder(BASE_URL)
        val authorizationService = retrofit.authorizationService
        val call = authorizationService.getAccessToken(httpBasicAuth, parameters)
        // Send the request synchronously.
        val response = call.execute()
        return if (response.isSuccessful) {
            Logger.log(TAG, "Successful response.")
            response.body()
        } else {
            Logger.log(TAG, "Error during authorization request. ${response.message()}")
            null
        }
    }


    fun refreshAccessToken() {
        Logger.log(TAG, "Refreshing access token.")
        val refreshToken = getRefreshToken()
        checkNotNull(refreshToken) {Logger.log(TAG, "No refresh token found.")}

        val httpBasicAuth = getHttpBasicAuth()
        val parameters = "grant_type=$GET_REFRESHED_TOKEN&refresh_token=$refreshToken"
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val accessToken = requestAccessToken(httpBasicAuth, parameters)
        checkNotNull(accessToken)
        FileTokenManager.saveAccessTokenToFile(accessToken)
    }

    /**
     * Gets the current refresh token from a file on disk.
     * @return The refresh token if it exists, null otherwise.
     */
    private fun getRefreshToken(): String? {
        val accessToken = FileTokenManager.getAccessTokenFromFile()
        return accessToken.refreshToken
    }


//    /**
//     * Prompts the user for authorization, parsers the redirect user for the access code, exchanges it for an access token,
//     * and saves it to a file.
//     */
//    fun getNewAccessToken() {
//        val accessTokenResponse = UserAuthorizationRequester().requestAuthorization()
//        println("Received access token = $accessTokenResponse")
//        FileTokenManager.saveAccessTokenToFile(accessTokenResponse)
//        printCurrentStoredToken()
//    }

//    fun printCurrentStoredToken() {
//        println("\nCurrent token in file:")
//        val currentToken = FileTokenManager.getAccessTokenFromFile()
//        println(currentToken)
//    }
//
//    /**
//     * Uses a stored refresh token to request a new one. This action does not require any input from the user, the
//     * application is already authorized.
//     */
//    fun refreshCurrentAccessToken() {
//        printCurrentStoredToken()
//        val refreshToken = FileTokenManager.getAccessTokenFromFile().refreshToken
//        val newAccessTokenResponse = AccessTokenResponseRetriever.getRenewedAccessTokenResponse(refreshToken)
//        val newAccessToken =
//            Klaxon().fieldRenamer(TokenResponseJsonRenamer).parse<AccessToken>(newAccessTokenResponse)
//        checkNotNull(newAccessToken)
//        println("\nNew Token:\n$newAccessToken")
//        FileTokenManager.saveAccessTokenToFile(newAccessToken)
//    }
}