package authorization

import constant.CLIENT_ID
import constant.REDIRECT_URI
import net.api.AuthorizationService
import net.model.AccessToken
import file.loadClientSecret
import file.loadToken
import file.saveToken
import net.RedirectUriResult
import net.buildService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

/**
 * grant_type that indicates that you're using the "standard" code based flow.
 * Used when requesting a new access token after being granted authorization by the user.
 */
private const val GET_NEW_TOKEN = "authorization_code"

/** grant_type that indicates you're requesting a new access token using a refresh token */
private const val GET_REFRESHED_TOKEN = "refresh_token"

/** Retrieves an access token using the standard "code flow". Will save the token to a file on disk. */
suspend fun codeFlowAuthorization() {
    val redirectUriResult = requestAuthorization()
    check(redirectUriResult is RedirectUriResult.Success) { "Bad redirect URI. Message = ${(redirectUriResult as RedirectUriResult.Error).message}" }

    val httpBasicAuth = getHttpBasicAuth()
    val requestParameters = getNewRequestParameters(redirectUriResult)
    val accessToken = requestAccessToken(httpBasicAuth, requestParameters)
    checkNotNull(accessToken)
    saveToken(accessToken)
}

/** Needed for the "Authorization" header of the request. */
private fun getHttpBasicAuth(): String {
    val clientId: String = CLIENT_ID
    val clientSecret: String = loadClientSecret()
    return getBasicAuth(clientId, clientSecret)
}

/**
 * Gets the specific parameters required for the POST request's body that asks
 * for a new access token.
 */
private fun getNewRequestParameters(redirectUriResult: RedirectUriResult.Success): RequestBody {
    val exchangeCode: String = redirectUriResult.code
    val redirectUri: String = REDIRECT_URI
    // Set the parameters Reddit requires in the POST request's body
    val parameters = "grant_type=$GET_NEW_TOKEN&code=$exchangeCode&redirect_uri=$redirectUri"
    // The parameters won't work if send as a plain string, they need to be converted.
    return parameters.toRequestBody("text/plain".toMediaTypeOrNull())
}

/**
 * Makes a POST request to Reddit asking for an access token. This function can be used both for
 * requesting a new access token, or refreshing an old one, as the only thing different between them
 * are the POST body [parameters]
 * @param httpBasicAuth Needed for the "Authorization" header of the request. See [HttpBasicAuthFormatter].
 * @param parameters See [getNewRequestParameters]
 * @return An [AccessToken] if the response is successful, null otherwise.
 */
private suspend fun requestAccessToken(httpBasicAuth: String, parameters: RequestBody): AccessToken? {
    println("Making a POST request to Reddit...")
    val authorizationService = buildService(AuthorizationService::class.java, isUsingOauth = false)
    val response: Response<AccessToken> = authorizationService.fetchAccessToken(httpBasicAuth, parameters)

    // Send the request asynchronously
    // Has to be inside a try block because the connection could fail.
    try {
        if (response.isSuccessful) {
            println("Successful response")
            return response.body()
        } else {
            println("Error during authorization request: ${response.code()}")
        }
    } catch (t: Throwable) {
        println("Error during authorization request: ${t.message}")
    }

    return null
}

suspend fun refreshAccessToken() {
    println("Refreshing access token")
    val refreshToken = loadToken().refreshToken
    checkNotNull(refreshToken) { "No refresh token found" }

    val httpBasicAuth = getHttpBasicAuth()
    val parameters = "grant_type=$GET_REFRESHED_TOKEN&refresh_token=$refreshToken"
        .toRequestBody("text/plain".toMediaTypeOrNull())

    val accessToken = requestAccessToken(httpBasicAuth, parameters)
    checkNotNull(accessToken)
    saveToken(accessToken)
}