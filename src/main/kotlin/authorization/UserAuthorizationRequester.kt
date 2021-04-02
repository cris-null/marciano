package authorization

import net.*
import string.RandomStringGenerator
import java.awt.Desktop
import java.net.URI

class UserAuthorizationRequester {

    private lateinit var state: String

    /**
     * Requests authorization to the user via the command line.
     *
     * Will asks to user to authorize the application in their web browser. For now,
     * the user will have to manually enter the redirect URI into the command line,
     * so that this program can parse it and get the code required to obtain an access token.
     *
     * @return The [RedirectUriResult] obtained
     */
    fun requestCliAuthorization(): RedirectUriResult {
        val authUrl = generateAuthUrl()
        println("Please allow authorization with Reddit.\nOpening browser...")
        openBrowser(authUrl)
        return requestRedirectUriFromUser()
    }

    private fun generateAuthUrl(): String {
        val state = RandomStringGenerator.getRandomString()
        return AuthorizationUrlGenerator.getAuthorizationUrl(state)
    }

    private fun openBrowser(url: String) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse( URI(url))
        }
    }

    private fun requestRedirectUriFromUser(): RedirectUriResult {
        println("Enter the redirect URI:")
        val redirectUri = readLine()
        while (redirectUri.isNullOrEmpty()) {
            println("Error! Enter the redirect URI:")
        }
        return RedirectUriParser.getRedirectUriResult(redirectUri)
    }
}