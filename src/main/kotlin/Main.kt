import util.authorization.AuthorizationUrlGenerator
import util.string.RandomStringGenerator
import util.string.RedirectUriParser
import util.string.RedirectUriResult

fun main() {
    val state = RandomStringGenerator.getRandomString()
    println(AuthorizationUrlGenerator.getAuthorizationUrl(state))

    println("Enter redirect URI:")
    val redirectUri = readLine()

    val redirectUriResult = RedirectUriParser.getRedirectUriResult(redirectUri!!)

    when (redirectUriResult) {
        is RedirectUriResult.Success -> {
            if (redirectUriResult.state == state) println("state matches!")
            println("code = ${redirectUriResult.code}")
        }
        is RedirectUriResult.Error -> {
            println("error msg = ${redirectUriResult.message}")
        }
    }
}