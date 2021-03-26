package net

import util.constant.RegisteredAppInformation
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object AccessTokenRetriever {

    // To retrieve the access token, Reddit requires a POST request to be made to this URL
    private const val OAUTH_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"

    /**
     * I don't understand very well how this works, but I tried using an online API testing tool
     * called [ReqBin](https://reqbin.com/) that has a GUI to make POST requests.
     *
     * It seems that "Authorization" is a header added to the POST request that can have many forms,
     * one of them being "Basic Auth" that accepts a username and a password. It seems that this is
     * the one Reddit uses, as they use those terms "username" and "password" when describing
     * how to send the client ID and the app secret.
     * @param secret The client secret given by Reddit after you register your app.
     * @param code The code retrieved from the redirect URI after a successful authorization.
     */
    fun getAuthToken(secret: String, code: String) {
        val url = URL(OAUTH_TOKEN_URL)
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "POST"

        val user = RegisteredAppInformation.CLIENT_ID
        val userCredentials = "$user:$secret"
        // It was not explained in the Reddit documentation but apparently it must be encoded in Base64
        // Not sure why I have to prefix the word "Basic". Maybe it's because it's "basic authentication"?
        // I think this is part of a web standard.
        val authorizationValue = "Basic " + String(Base64.getEncoder().encode(userCredentials.toByteArray()))

        // It seems that the only header required by Reddit is "Authorization".
        con.setRequestProperty("Authorization", authorizationValue)
        // In the 'Error" section the say to make sure the content type of the HTTP message is set to this.
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        // This is not needed I think but just to be sure I put it in. I'm not sure what exactly I should write here.
        con.setRequestProperty("User-Agent", "Kotlin test script")


        // Send post request
        con.doOutput = true
        val dataOutputStream = DataOutputStream(con.outputStream)
        // Reddit requires these parameters in the POST data (not as part of the URL)
        // Reference: https://github.com/reddit-archive/reddit/wiki/OAuth2#retrieving-the-access-token
        val urlParameters =
            "grant_type=authorization_code&code=$code&redirect_uri=${RegisteredAppInformation.REDIRECT_URI}"
        dataOutputStream.writeBytes(urlParameters)
        dataOutputStream.flush()
        dataOutputStream.close()
        val responseCode = con.responseCode
        println("\nSending POST request...")
        println("Response code = $responseCode")

        // Print the response from the request to the console
        var content: StringBuilder
        BufferedReader(
            InputStreamReader(con.inputStream)
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