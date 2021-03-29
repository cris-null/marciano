package net

import constant.RegisteredAppInformation

/**
 * When the user is send to the authorization URL, they will be told to allow or deny access to your application.
 * After doing so, the browser will be instructed to redirected to your application's registered `redirect_uri`, which
 * will have information as query parameters, which need to be parsed.
 */
object RedirectUriParser {

    fun getRedirectUriResult(redirectUri: String): RedirectUriResult {
        return if (redirectUri.contains("error="))
            parseErrorUri(redirectUri)
        else
            parseSuccessUri(redirectUri)
    }

    /**
     * Assumes that the redirect URI contains the error parameter which means there was an authorization error.
     *
     * Check the [documentation](https://github.com/reddit-archive/reddit/wiki/OAuth2) to see the types.
     */
    private fun parseErrorUri(redirectUri: String): RedirectUriResult.Error {
        // Example of what a redirect URI looks like when the user denies access:
        // http://localhost:8080/?state=a8f33b12-e4f4-44a0-bcfa-75c01e7b3c14&error=access_denied#_
        val errorUri = stripClutter(redirectUri)
        val message = errorUri.substringAfterLast("error=")
        return RedirectUriResult.Error(message)
    }

    /**
     * Assumes that if the redirect URI does not contain the error parameter, then the authorization was successful.
     */
    private fun parseSuccessUri(redirectUri: String): RedirectUriResult.Success {
        // Example of what a redirect URI looks like when the user granted access:
        // http://localhost:8080/?state=4dea32db-d4fc-4f3a-b686-26fc3d18e45f&code=HExMBPuoKiUW3togL7YCXH7OJmY4sg#_
        val successUri = stripClutter(redirectUri)
        val code = successUri.substringAfterLast("code=")
        val state = successUri.substringAfter("state=").substringBefore("&code=")
        return RedirectUriResult.Success(state, code)
    }

    /**
     * A redirect URI contains useless information such as the domain and port at the start, and a fix fragment at the end.
     * This returns the URI without that clutter.
     */
    private fun stripClutter(redirectUri: String): String {
        return redirectUri.removePrefix("${RegisteredAppInformation.REDIRECT_URI}/?").removeSuffix("#_")
    }
}

sealed class RedirectUriResult {

    data class Success(val state: String, val code: String) : RedirectUriResult()
    data class Error(val message: String) : RedirectUriResult()
}