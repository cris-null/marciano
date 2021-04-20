package net.api

import net.model.AccessToken
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthorizationService {

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: marciano Kotlin/1.4"
    )
    @POST("/api/v1/access_token")
    /**
     * Note that the base URL for this request is https://www.reddit.com.
     *
     * Makes a POST request to Reddit. If successful response will contain an access token,
     * and possibly a refresh token.
     *
     * @param parameters The parameter to be included in the post data.
     *
     * If retrieving the access token after being authorized by the user,
     * use:
     *
     * grant_type=authorization_code&code=CODE&redirect_uri=URI
     *
     * Where CODE is the code you retrieved from the redirect URI; and
     * URI is the same redirect uri (exact match) you used when you registered your app
     * with Reddit.
     *
     * If refreshing your expired or soon-to-expire access token, use:
     *
     * grant_type=refresh_token&refresh_token=TOKEN
     *
     * @param httpBasicAuth HTTP Basic Auth as defined by the RFC spec.
     *
     * It looks like this:
     *
     * Basic some_base64_encoded_string
     *
     * some_base64_encoded_string is your client ID and client secret, like this:
     *
     * clientId:secret
     *
     * But you need to transform it to a byte array and then base 64 encode it.
     * Example:
     *
     * String(Base64.getEncoder().encode(myString.toByteArray()))
     */
    suspend fun getAccessToken(
        @Header("Authorization") httpBasicAuth: String,
        @Body parameters: RequestBody
    ): Response<AccessToken>
}