package net

/**
 * A client's user agent should be unique and descriptive, according
 * to the [official guidelines](https://github.com/reddit-archive/reddit/wiki/API).
 */
fun makeUserAgent(): String {
    return "multi-platform:net.csalazar.marciano:v0.3 (by /u/cris_null)"
}