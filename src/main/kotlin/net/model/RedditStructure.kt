package net.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

/** Describes the data structure of objects returned when using the Reddit JSON API. */

interface Votable {
    /* The number of upvotes. Includes own. */
    val ups: Int

    /* The number of downvotes. Includes own. */
    val downs: Int

    /**
     * True if thing is liked by the user, false if thing is disliked,
     * null if the user has not voted or you are not logged in.
     */
    val likes: Boolean?
}

interface Created {
    /**
     * The time of creation in local epoch-second format. ex: 1331042771.0
     *
     * Reddit says that the type is a [Long] but since it ends in ".0" you
     * must store it as a double. However, in some tests, the response did not
     * have a double, so maybe Long is fine? Todo: test if it's fine to use Long instead of Double.
     */
    val created: Long

    /**
     * The time of creation in UTC epoch-second format. Note that neither of these ever have a non-zero fraction.
     */
    val createdUtc: Long
}

