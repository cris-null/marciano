package util.constant

object RegisteredAppInformation {


    /**
     * The Client ID generated during app registration.
     *
     * Tells reddit.com which app is making the request.
     */
    const val CLIENT_ID = "ZFRtiQMXqo4hAA"

    /**
     * Should be the string "code". It should literally be that string.
     */
    const val RESPONSE_TYPE = "code"

    /**
     * The redirect_uri you have specified during registration.
     *
     * If this does not match the registered redirect_uri, the authorization request will fail.
     * If authorization succeeds, the user's browser will be instructed to redirect to this location.
     */
    const val REDIRECT_URI = "http://localhost:8080"

    /**
     * Either "temporary" or "permanent".
     * Indicates whether or not your app needs a permanent token.
     *
     * All bearer tokens expire after 1 hour. If you indicate you need permanent access to a user's account,
     * you will additionally receive a refresh_token when acquiring the bearer token.
     * You may use the refresh_token to acquire a new bearer token after your current token expires.
     * Choose temporary if you're completing a one-time request for the user (such as analyzing their recent comments);
     * choose permanent if you will be performing ongoing tasks for the user, such as notifying them whenever
     * they receive a private message. The implicit grant flow does not allow permanent tokens.
     */
    const val DURATION = "temporary"

    /**
     * A space-separated* list of scope strings (commas are supported too).
     *
     * All bearer tokens are limited in what functions they may perform.
     *
     * You must explicitly request access to areas of the api, such as private messaging or moderator actions.
     * See our automatically generated API docs.
     *
     * Scope Values:
     * identity, edit, flair, history, modconfig, modflair, modlog, modposts, modwiki, mysubreddits,
     * privatemessages, read, report, save, submit, subscribe, vote, wikiedit, wikiread.
     */
    const val SCOPE = "identity,history"
}