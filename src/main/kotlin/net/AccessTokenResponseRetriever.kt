package net

import constant.RegisteredAppInformation
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object AccessTokenResponseRetriever {

    // To retrieve the access token, Reddit requires a POST request to be made to this URL
    private const val OAUTH_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"

    /**
     * @param secret The client secret given by Reddit after you register your app.
     * @param code The code retrieved from the redirect URI after a successful authorization.
     * @return A String representing a JSON response with the access token, among other things.
     */
    fun getTokenResponseJson(secret: String, code: String): String {
        val postConnection = setupPostConnection(secret)
        makePostRequest(postConnection, code)
        return getRequestResponse(postConnection)
    }

    /**
     * I don't understand very well how this works, but I tried using an online API testing tool
     * called [ReqBin](https://reqbin.com/) that has a GUI to make POST requests.
     *
     * It seems that "Authorization" is a header added to the POST request that can have many forms,
     * one of them being "Basic Auth" that accepts a username and a password. It seems that this is
     * the one Reddit uses, as they use those terms "username" and "password" when describing
     * how to send the client ID and the app secret.
     *
     * This functions uses what I've learned to setup a connection anticipating a POST request to Reddit.
     *
     * @param secret The client secret given by Reddit after you register your app.
     * @return A [HttpURLConnection] which the correct "headers" (I think that's what those are)
     */
    private fun setupPostConnection(secret: String): HttpURLConnection {
        val url = URL(OAUTH_TOKEN_URL)
        val postConnection = url.openConnection() as HttpURLConnection
        postConnection.requestMethod = "POST"

        val user = RegisteredAppInformation.CLIENT_ID
        val userCredentials = "$user:$secret"
        // It was not explained in the Reddit documentation but apparently it must be encoded in Base64
        // Not sure why I have to prefix the word "Basic". Maybe it's because it's "basic authentication"?
        // I think this is part of a web standard.
        val authorizationValue = "Basic " + String(Base64.getEncoder().encode(userCredentials.toByteArray()))

        // It seems that the only header required by Reddit is "Authorization".
        postConnection.setRequestProperty("Authorization", authorizationValue)
        // In the 'Error" section the say to make sure the content type of the HTTP message is set to this.
        postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        // This is not needed I think but just to be sure I put it in. I'm not sure what exactly I should write here.
        postConnection.setRequestProperty("User-Agent", "Kotlin test script")

        return postConnection
    }

    /**
     * Makes a POST request using the required parameter in the body of the request. These
     * parameters are dictated by Reddit.
     */
    private fun makePostRequest(postConnection: HttpURLConnection, code: String) {
        postConnection.doOutput = true
        val dataOutputStream = DataOutputStream(postConnection.outputStream)
        // Reddit requires these parameters in the POST data (not as part of the URL)
        // Reference: https://github.com/reddit-archive/reddit/wiki/OAuth2#retrieving-the-access-token
        val urlParameters =
            "grant_type=authorization_code&code=$code&redirect_uri=${RegisteredAppInformation.REDIRECT_URI}"
        dataOutputStream.writeBytes(urlParameters)
        dataOutputStream.flush()
        dataOutputStream.close()
        val responseCode = postConnection.responseCode
        println("\nSending POST request...")
        println("Response code = $responseCode")
    }

    /**
     * After a successful POST request, Reddit will return a JSON response that looks like this:
     *
     * {"access_token": "480452782549-CTxdOKN5YsoDSNYsGvqkouqhhVlZJw", "token_type": "bearer", "expires_in": 3600, "refresh_token": "480452782549-gk0HFm11OBIcB4mtaDDAtMgtGAaifg", "scope": "history identity"}
     *
     * @return A String representing a JSON response with the access token, among other things.
     */
    private fun getRequestResponse(postConnection: HttpURLConnection): String {
        var content: StringBuilder
        BufferedReader(
            InputStreamReader(postConnection.inputStream)
        ).use { br ->
            var line: String?
            content = StringBuilder()
            while (br.readLine().also { line = it } != null) {
                content.append(line)
                content.append(System.lineSeparator())
            }
        }
        return content.toString()
    }
}