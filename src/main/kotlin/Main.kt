import net.AccessTokenResponseRetriever
import net.TokenResponseParser
import authorization.AuthorizationUrlGenerator
import file.FileTokenManager
import string.RandomStringGenerator
import net.RedirectUriParser
import net.RedirectUriResult

fun main() {

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
        val responseJson = AccessTokenResponseRetriever.getAccessTokenResponse(secret!!, code)
        val responseObject = TokenResponseParser.parse(responseJson)
        println(responseObject)
    }
}