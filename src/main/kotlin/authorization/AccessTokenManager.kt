package authorization

import Logger
import constant.RegisteredAppInformation
import data.api.RetrofitBuilder
import data.model.AccessToken
import file.FileSecretReader
import net.RedirectUriResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object AccessTokenManager {

    /** For request that do not require a token */
    private const val BASE_URL = "https://www.reddit.com"

    /** If the request requires a token you MUST use this */
    private const val OAUTH_URL = "https://oauth.reddit.com"

    /**
     * Indicates that you're using the "standard" code based flow. Used when requesting a new
     * access token after being granted authorization by the user.
     */
    private const val GET_NEW_TOKEN = "authorization_code"

    private val TAG = javaClass.simpleName

    fun getNewAccessToken(): AccessToken? {
        val redirectUriResult = UserAuthorizationRequester.request()
        if (redirectUriResult is RedirectUriResult.Error) {
            Logger.log(TAG, "Bad redirect URI. Message = ${redirectUriResult.message}")
            return null
        }

        // If it's not an error, you got the exchange code
        check(redirectUriResult is RedirectUriResult.Success)
        val exchangeCode: String = redirectUriResult.code
        val redirectUri: String = RegisteredAppInformation.REDIRECT_URI
        // Set the parameters Reddit requires in the POST request's body
        val parameters = "grant_type=$GET_NEW_TOKEN&code=$exchangeCode&redirect_uri=$redirectUri"

        // These are needed for the "Authorization" header of the request.
        val clientId: String = RegisteredAppInformation.CLIENT_ID
        val clientSecret: String = FileSecretReader.getClientSecret()
        // This is the value that will accompany the "Authorization" header.
        val basicAuth: String = HttpBasicAuthGetter.getBasicAuth(clientId, clientSecret)

        // All the requirements have been fulfilled, time to make the request.
        val retrofit = RetrofitBuilder(BASE_URL)
        val authService = retrofit.authorizationService
        // The parameters won't work if send as a plain string, they need to be converted.
        val postParameters: RequestBody = parameters.toRequestBody("text/plain".toMediaTypeOrNull())
        val call = authService.getAccessToken(basicAuth, postParameters)

        var accessToken: AccessToken? = null

        val response = call.execute()
        if (response.isSuccessful) {
            Logger.log(TAG, "Successful response.")
            accessToken = response.body()
        } else {
            Logger.log(TAG, "Error during authorization request. ${response.message()}")
        }

        return accessToken
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