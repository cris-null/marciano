import net.AccessTokenResponseRetriever
import util.authorization.AuthorizationUrlGenerator
import util.string.RandomStringGenerator
import util.string.RedirectUriParser
import util.string.RedirectUriResult

fun main() {
    testGetAccessToken()
}

fun testGetAccessToken() {
    val state = RandomStringGenerator.getRandomString()
    println(AuthorizationUrlGenerator.getAuthorizationUrl(state))

    println("Enter redirect URI:")
    val redirectUri = readLine()

    val redirectUriResult = RedirectUriParser.getRedirectUriResult(redirectUri!!)

    var code: String? = null
    when (redirectUriResult) {
        is RedirectUriResult.Success -> {
            if (redirectUriResult.state == state) println("state matches!")
            println("code = ${redirectUriResult.code}")
            code = redirectUriResult.code
        }
        is RedirectUriResult.Error -> {
            println("error msg = ${redirectUriResult.message}")
        }
    }

    if (code != null) {

        println("Enter you app's secret:")
        val secret = readLine()
        println(AccessTokenResponseRetriever.getAccessTokenResponse(secret!!, code))
    }
}