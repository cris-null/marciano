package authorization

import constant.*

/**
 * @param state A string of your choosing. You should generate a unique, possibly random,
 * string for each authorization request. This value will be returned to you when the user
 * visits your REDIRECT_URI after allowing your app access - you should verify that it
 * matches the one you sent. This ensures that only authorization requests you've started are
 * ones you finish. (You may also use this value to, for example, tell your webserver what action
 * to take after receiving the OAuth2 bearer token)
 * @param duration See [RegisteredAppInformation.DURATION]
 * @param scope See [RegisteredAppInformation.SCOPE]
 */
fun getAuthorizationUrl(
    state: String,
    duration: String = DURATION,
    scope: String = SCOPE
): String {
    return "https://www.reddit.com/api/v1/authorize?client_id=$CLIENT_ID&response_type=$RESPONSE_TYPE" +
            "&state=$state&redirect_uri=$REDIRECT_URI&duration=$duration&scope=$scope"
}