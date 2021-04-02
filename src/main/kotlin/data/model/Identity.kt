package data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The identity of the user.
 *
 * Endpoint: /api/v1/me
 * Scope: identity
 */
@JsonClass(generateAdapter = true)
data class Identity(
    val name: String,
    /**
     * When the account was created, in Unix
     * Epoch seconds.
     */
    val created: Long,
    @Json(name = "comment_karma")
    val commentKarma: Long,
    @Json(name = "link_karma")
    val linkKarma: Long,
)
