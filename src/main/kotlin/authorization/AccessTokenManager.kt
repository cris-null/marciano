package authorization

import com.beust.klaxon.Klaxon
import file.FileTokenManager
import net.AccessToken
import net.TokenResponseJsonRenamer
import net.AccessTokenResponseRetriever

object AccessTokenManager {

    fun getNewAccessToken() {

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

    fun printCurrentStoredToken() {
        println("\nCurrent token in file:")
        val currentToken = FileTokenManager.getAccessTokenFromFile()
        println(currentToken)
    }

    /**
     * Uses a stored refresh token to request a new one. This action does not require any input from the user, the
     * application is already authorized.
     */
    fun refreshCurrentAccessToken() {
        printCurrentStoredToken()
        val refreshToken = FileTokenManager.getAccessTokenFromFile().refreshToken
        val newAccessTokenResponse = AccessTokenResponseRetriever.getRenewedAccessTokenResponse(refreshToken)
        val newAccessToken =
            Klaxon().fieldRenamer(TokenResponseJsonRenamer).parse<AccessToken>(newAccessTokenResponse)
        checkNotNull(newAccessToken)
        println("\nNew Token:\n$newAccessToken")
        FileTokenManager.saveAccessTokenToFile(newAccessToken)
    }
}