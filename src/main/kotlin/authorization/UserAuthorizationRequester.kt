package authorization

import net.*
import string.RandomStringGenerator

class UserAuthorizationRequester {

    private lateinit var state: String

    /**
     * Prompts the user to give authorization and if granted returns the
     */
    fun requestAuthorization(): AccessToken {
        printAuthorizationPromptToConsole()
        val redirectUriResult = requestRedirectUriFromUser()
        return getTokenResponse(redirectUriResult)
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

    private fun getTokenResponse(redirectUriResult: RedirectUriResult): AccessToken {
        when (redirectUriResult) {
            // User authorized the application.
            is RedirectUriResult.Success -> {
                // Reddit recommends to check that the state matches
                if (redirectUriResult.state == state)
                    println("Authorization response state matches!")

                return parseTokenResponse(redirectUriResult.code)
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
    private fun parseTokenResponse(code: String): AccessToken {
        val responseJson: String =
            AccessTokenResponseRetriever.getNewAccessTokenResponse(code)
        val reponse: AccessToken =
            AccessTokenResponseParser.parse(responseJson) ?: throw Exception("Error while parsing JSON response")
        return reponse
    }
}