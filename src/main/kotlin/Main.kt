import authorization.AccessTokenExpirationWatchdog
import authorization.AccessTokenManager
import com.beust.klaxon.Klaxon
import file.FileTokenManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.IdentityGetter
import java.io.File

fun main(): Unit = runBlocking {
//    launch {
//        AccessTokenExpirationWatchdog.monitorAccessTokenExpiration(1800, 30000)
//    }

//    val accessToken = FileTokenManager.getAccessTokenFromFile().accessToken
//    IdentityGetter().getUserIdentity(accessToken)

    val testJsonFile = File("test.json")
    val testJson = testJsonFile.readText()
    val graphicAPIResponse = Klaxon().parse<GraphicAPIResponse>(testJson)
    println(graphicAPIResponse)
}

data class GraphicAPIResponse(
    var code: Int,
    var status: String,
    var errorMessage: String = "",
    var FirstDay: String = "",
    var LastDay: String = "",
    var graphic: Map<String, Lesson> = mapOf()
)

class Lesson(val godzinaStart: String, val godzinaStop: String)