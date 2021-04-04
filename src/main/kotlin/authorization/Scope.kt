package authorization

/**
 * All bearer tokens are limited in what functions they may perform.
 *
 * You must explicitly request access to areas of the api, such as private messaging or moderator actions.
 *
 * When using the scopes as a parameter, remember that they must be separated by one space (although commas
 * are also supported).
 *
 * Check [here](http://www.reddit.com/dev/api/oauth) to see which endpoints require which scopes.
 *
 * Check [here](https://www.reddit.com/api/v1/scopes) to see all the scopes with their descriptions.
 */
enum class Scope {
    IDENTITY, EDIT, FLAIR, HISTORY, MOD_CONFIG, MOD_FLAIR,
    MOD_LOG, MOD_POSTS, MOD_WIKI, MY_SUBREDDITS, PRIVATE_MESSAGES,
    READ, REPORT, SAVE, SUBMIT, SUBSCRIBE, VOTE, WIKI_EDIT, WIKI_READ;

    /**
     * The API scopes designed by Reddit are all lowercase and concatenated.
     *
     * The enums in this class have underscores for readability but should be removed
     * when actually sending them to Reddit.
     */
    override fun toString(): String {
        return super.toString().toLowerCase().replace("_", "")
    }
}