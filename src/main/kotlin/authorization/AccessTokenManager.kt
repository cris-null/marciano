package authorization

import Logger
import constant.RegisteredAppInformation
import data.api.RetrofitBuilder
import data.model.AccessToken
import file.FileSecretReader
import net.RedirectUriResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AccessTokenManager {

    private const val BASE_URL = "https://www.reddit.com"
    private const val OAUTH_URL = "https://oauth.reddit.com"

    private val tag = this.javaClass.simpleName

    fun getNewAccessToken(): AccessToken? {
        val redirectUriResult = UserAuthorizationRequester.request()
        if (redirectUriResult is RedirectUriResult.Error) {
            Logger.log(tag, "Bad redirect URI. Message = ${redirectUriResult.message}")
            return null
        }

        // If it's not an error, you got the exchange code
        val exchangeCode: String = (redirectUriResult as RedirectUriResult.Success).code
        val redirectUri: String = RegisteredAppInformation.REDIRECT_URI
        val parameters = "grant_type=authorization_code&code=$exchangeCode&redirect_uri=$redirectUri"

        val clientId: String = RegisteredAppInformation.CLIENT_ID
        val clientSecret: String = FileSecretReader.getClientSecret()
        val basicAuth: String = HttpBasicAuthGetter.getBasicAuth(clientId, clientSecret)

        val authService = RetrofitBuilder(BASE_URL).authorizationService
        val call = authService.getAccessToken(basicAuth, parameters)

        var accessToken: AccessToken? = null
        call.enqueue(object: Callback<AccessToken> {
            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    Logger.log(tag, "Successful response.")
                    accessToken = response.body()
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Logger.log(tag, "Error during authorization request. ${t.message}")
            }
        })

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