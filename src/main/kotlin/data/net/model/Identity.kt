package data.net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The identity of the user.
 *
 * Endpoint: /api/v1/me
 * Scope: identity
 */
@Serializable
data class Identity(
    val name: String,
    /**
     * When the account was created, in Unix
     * Epoch seconds.
     */
    val created: Long,
    @SerialName("comment_karma")
    val commentKarma: Long,
    @SerialName("link_karma")
    val linkKarma: Long,
)
