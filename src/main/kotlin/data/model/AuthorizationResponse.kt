package data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class that represent the response send by Reddit when you successfully
 * authorize and receive an authorization token.
 *
 * Note that depending on whether you requested a permanent authorization, there might
 * or might not be a refresh token included in the response.
 *
 * Note that the JSON parsing library Moshi has support for Kotlin via code-gen which allows
 * nullable types to be declared in a JSON model class. This means that an error will be thrown
 * if a non-nullable property is in the class but not in the JSON. However, if a nullable property
 * is in the class and not in the JSON, then the value will just be set to null.
 *
 * This means that this single can handle both kinds of authorization response, those that contain
 * a refresh token, and those that don't.
 */
@JsonClass(generateAdapter = true)
data class AuthorizationResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "expires_in")
    val secondsTillExpiration: Int,
    @Json(name = "refresh_token")
    val refreshToken: String?,
    val scope: String
)