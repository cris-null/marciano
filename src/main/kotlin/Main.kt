import authorization.checkAccessTokenExpiration
import file.loadToken
import kotlinx.coroutines.runBlocking
import net.helper.fetchSaved
import net.model.Listing

suspend fun getAuthParam(): String {
    val accessToken = loadToken().accessToken
    return "Bearer $accessToken"
}

fun main(): Unit = runBlocking {
    checkAccessTokenExpiration(1800)
    val savedPosts = fetchSaved(getAuthParam(), "cris_null")
    val listing = savedPosts.body()


    println(listing?.let { listingWalk(it) })
}

var count = 0

suspend fun listingWalk(listing: Listing, list: MutableList<Listing>) {

    return if (listing.next == null) {
    } else {
        list.add(listing)
        val nextListing = fetchSaved(getAuthParam(), "cris_null", listing.next)
        listingWalk(nextListing.body()!!, list)
    }
}