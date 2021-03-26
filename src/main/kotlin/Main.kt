import net.AccessTokenResponseRetriever
import net.TokenResponseParser
import authorization.AuthorizationUrlGenerator
import constant.RegisteredAppInformation
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
        val responseJson = AccessTokenResponseRetriever.getAccessTokenResponse(RegisteredAppInformation.SECRET, code)
        val responseObject = TokenResponseParser.parse(responseJson)
        println(responseObject)
    }
}