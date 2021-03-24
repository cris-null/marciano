import util.authorization.AuthorizationUrlGenerator
import util.string.RandomStringGenerator

fun main() {
    val state = RandomStringGenerator.getRandomString()
    println(AuthorizationUrlGenerator.getAuthorizationUrl(state))
}