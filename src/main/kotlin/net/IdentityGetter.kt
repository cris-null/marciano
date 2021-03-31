package net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Gets the identity of the currently logged in user.
 */
class IdentityGetter {

    private val oauthUrl = "https://oauth.reddit.com"
    private val identityEndPoint = "/api/v1/me"

    fun getUserIdentity(accessToken: String) {
        val url = URL(oauthUrl + identityEndPoint)
        val getRequest = url.openConnection() as HttpURLConnection
        getRequest.requestMethod = "GET"
        getRequest.setRequestProperty("Authorization", "bearer $accessToken")
        getRequest.setRequestProperty("User-Agent", "Kotlin test script")

        var content: StringBuilder
        BufferedReader(
            InputStreamReader(getRequest.inputStream)
        ).use { br ->
            var line: String?
            content = StringBuilder()
            while (br.readLine().also { line = it } != null) {
                content.append(line)
                content.append(System.lineSeparator())
            }
        }
         println(content.toString())
    }
}