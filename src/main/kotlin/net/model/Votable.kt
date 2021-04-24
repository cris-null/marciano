package net.model

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