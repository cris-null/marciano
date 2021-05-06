import authorization.checkAccessTokenExpiration
import file.loadToken
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import net.configuredJson
import net.helper.fetchSaved
import net.model.Comment
import net.model.Link
import net.model.Listing
import net.model.Post
import java.io.File

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

    val response = fetchSaved(getAuthParam(), "cris_null")
    val listing = response.body()
//    val response = File("response.json").readText()
//    val listing = configuredJson.decodeFromString<Listing>(response)

    listing?.posts?.forEach {
        if (it is Link)
            println(it.media)
    }
}