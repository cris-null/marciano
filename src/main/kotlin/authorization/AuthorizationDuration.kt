package authorization

/**
 * All bearer tokens expire after 1 hour.
 *
 * If you indicate you need permanent access to a user's account,
 * you will additionally receive a refresh_token when acquiring the access token.
 *
 * You may use the refresh_token to acquire a new bearer token after your current token expires.
 *
 * Choose temporary if you're completing a one-time request for the user (such as analyzing their recent comments);
 * choose permanent if you will be performing ongoing tasks for the user, such as notifying them whenever
 * they receive a private message.
 */
enum class AuthorizationDuration {
    TEMPORARY, PERMANENT;

    override fun toString(): String {
        // The specific string Reddit requires as a parameter
        // is either "temporary" or "permanent".
        return super.toString().toLowerCase()
    }
}