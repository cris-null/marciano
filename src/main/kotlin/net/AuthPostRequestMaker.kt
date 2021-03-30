package net

import constant.RegisteredAppInformation
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * To retrieve the access token, Reddit requires a POST request to be made to this URL
 */
private const val OAUTH_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"

/**
 * Sets the connection required for authorization with Reddit when making a POST request for an auth token.
 *
 * Uses "HTTP BASIC Authentication" which is a RFC standard.
 */
class AuthPostRequestMaker(private val postParameters: String, private val address: String = OAUTH_TOKEN_URL) {

    private val postRequest: HttpURLConnection = initRequest()

    /**
     * Makes a POST request to Reddit requesting an access token. There are different kinds of tokens, and the
     * response returned by Reddit depends on the parameters of the POST requests' data.
     *
     * @param address The URL where the POST request should be made to.
     * @return The response returned by Reddit.
     */
    fun requestAccessTokenFromReddit(): String {
        setupHeaders()
        sendPostRequest()
        return getRequestResponse()
    }

    private fun initRequest(): HttpURLConnection {
        val url = URL(address)
        val postRequest = url.openConnection() as HttpURLConnection
        postRequest.requestMethod = "POST"
        return postRequest
    }

    /**
     * Sets up the headers as required by Reddit.
     */
    private fun setupHeaders() {
        val user = RegisteredAppInformation.CLIENT_ID
        val userCredentials = "$user:${RegisteredAppInformation.SECRET}"
        // It was not explained in the Reddit documentation but apparently it must be encoded in Base64
        // Not sure why I have to prefix the word "Basic".
        val authorizationValue = "Basic " + String(Base64.getEncoder().encode(userCredentials.toByteArray()))

        // It seems that the only header required by Reddit is "Authorization".
        postRequest.setRequestProperty("Authorization", authorizationValue)
        // In the 'Error" section the say to make sure the content type of the HTTP message is set to this,
        // so I'm leaving this here just in case.
        postRequest.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        // This is not needed (it wasn't requested by Reddit) but just to be a good citizen I put it in.
        // I'm not sure what exactly I should write here.
        postRequest.setRequestProperty("User-Agent", "Kotlin test script")
    }

    /**
     * Sends a POST request using the required parameter in the body of the request. These
     * parameters are dictated by Reddit.
     */
    private fun sendPostRequest() {
        postRequest.doOutput = true
        val dataOutputStream = DataOutputStream(postRequest.outputStream)
        println("\nSending POST request...")
        // These parameters are part of the POST data.
        dataOutputStream.writeBytes(postParameters)
        dataOutputStream.flush()
        dataOutputStream.close()
        val responseCode = postRequest.responseCode
        println("Response code = $responseCode")
    }

    /**
     * After a successful POST request, Reddit will return a JSON response that looks like this:
     *
     * {"access_token": "480452782549-CTxdOKN5YsoDSNYsGvqkouqhhVlZJw", "token_type": "bearer", "expires_in": 3600, "refresh_token": "480452782549-gk0HFm11OBIcB4mtaDDAtMgtGAaifg", "scope": "history identity"}
     *
     * @return A String representing a JSON response with the access token, among other things.
     */
    private fun getRequestResponse(): String {
        var content: StringBuilder
        BufferedReader(
            InputStreamReader(postRequest.inputStream)
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