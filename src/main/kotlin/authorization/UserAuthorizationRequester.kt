package authorization

import net.RedirectUriParser
import net.RedirectUriResult
import string.RandomStringGenerator

object UserAuthorizationRequester {

    /**
     * Requests authorization to the user via the command line.
     *
     * Will asks to user to authorize the application in their web browser. For now,
     * the user will have to manually enter the redirect URI into the command line,
     * so that this program can parse it and get the code required to obtain an access token.
     * */
    fun request(): RedirectUriResult {
        // You should generate a unique, possibly random, string for each authorization request.
        // This value will be returned to you when the user visits your REDIRECT_URI after allowing
        // your app access - you should verify that it matches the one you sent.
        // This ensures that only authorization requests you've started are ones you finish.
        val state = RandomStringGenerator.getRandomString()

        val authUrl = AuthorizationUrlGenerator.getAuthorizationUrl(state)
        println("Please allow authorization with Reddit.\nOpen this link in your browser:\n$authUrl")
        return requestRedirectUriFromUser(state)
    }

    private fun requestRedirectUriFromUser(state: String): RedirectUriResult {
        println("Enter the redirect URI:")
        val redirectUri = readLine()
        while (redirectUri.isNullOrEmpty()) {
            println("Error! Enter the redirect URI:")
        }
        return RedirectUriParser.parse(redirectUri, state)
    }
}