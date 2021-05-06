import authorization.checkAccessTokenExpiration
import file.loadToken
import kotlinx.coroutines.runBlocking
import net.helper.fetchSaved
import net.model.Comment
import net.model.Link
import net.model.Listing
import net.model.Post

suspend fun getAuthParam(): String {
    val accessToken = loadToken().accessToken
    return "Bearer $accessToken"
}

suspend fun printAllSavedPosts() {
    val savedPosts = fetchSaved(getAuthParam(), "cris_null")
    val listing = savedPosts.body()

    val listings = mutableListOf<Listing>()
    listing?.collectAll(listing, listings)

    val posts = mutableListOf<Post>()
    listings.forEach {
        it.posts.forEach { post ->
            posts.add(post)
        }
    }

    println(posts.size)
    posts.forEach {
        if (it is Comment) {
            println("""
                
                ==== COMMENT ====
                
            """.trimIndent())
            println(it.body)
        } else if (it is Link) {
            println("""
                
                ==== LINK ====
                
            """.trimIndent())
            println(it.title)
        }
    }
}

fun main(): Unit = runBlocking {
    checkAccessTokenExpiration(1800)
}