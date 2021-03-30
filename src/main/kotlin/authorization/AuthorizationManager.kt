package authorization

import net.*
import string.RandomStringGenerator

class AuthorizationManager {

    private lateinit var state: String

    fun authorize(): RefreshTokenResponse {
        printAuthorizationPromptToConsole()
        val redirectUriResult = requestRedirectUriFromUser()
        return getRefreshTokenResponse(redirectUriResult)
    }

    private fun printAuthorizationPromptToConsole() {
        state = RandomStringGenerator.getRandomString()
        val authorizationUrl = AuthorizationUrlGenerator.getAuthorizationUrl(state)
        println("Authorize application in the following URL: \n$authorizationUrl")
    }

    private fun requestRedirectUriFromUser(): RedirectUriResult {
        println("Enter the redirect URI:")
        val redirectUri = readLine()
        while (redirectUri.isNullOrEmpty()) {
            println("Error! Enter the redirect URI:")
        }
        return RedirectUriParser.getRedirectUriResult(redirectUri)
    }

    private fun getRefreshTokenResponse(redirectUriResult: RedirectUriResult): RefreshTokenResponse {
        when (redirectUriResult) {
            // User authorized the application.
            is RedirectUriResult.Success -> {
                // Reddit recommends to check that the state matches
                if (redirectUriResult.state == state)
                    println("Authorization response state matches!")

                return parseRefreshTokenResponse(redirectUriResult.code)
            }
            // User probably denied access. Check docs for the other cases.
            is RedirectUriResult.Error -> {
                throw Exception("Authorization Error! Message: ${redirectUriResult.message}")
            }
        }
    }

    /**
     * TODO this function should:
     *
     * be renamed
     * refactor so as to not throw an exception but instead work with nulls
     *
     * @throws Exception If the JSON response could not be parsed correctly.
     */
    private fun parseRefreshTokenResponse(code: String): RefreshTokenResponse {
        val responseJson: String =
            AuthorizationResponseRetriever.getAuthorizationResponse(code)
        val reponse: RefreshTokenResponse =
            TokenResponseParser.parse(responseJson) ?: throw Exception("Error while parsing JSON response")
        return reponse
    }
}