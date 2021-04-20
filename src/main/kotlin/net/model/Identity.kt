package net.model

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
    @SerialName("comment_karma")
    val commentKarma: Long,
    @SerialName("link_karma")
    val linkKarma: Long,
)
