import authorization.codeFlowAuthorization
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
//    checkAccessTokenExpiration(1800)

    codeFlowAuthorization()

    // ==============================================
    // GETTING TROPHIES TEST
    // ==============================================

//    val accessToken: String = loadToken().accessToken
//    val authorizationParameter = "Bearer $accessToken"
//    val response = getTrophies(authorizationParameter)
//    if (response.isSuccessful) {
//        val trophies = response.body()
//        println(trophies)
//    }
}