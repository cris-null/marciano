import authorization.AuthorizationManager
import file.FileTokenManager

fun main() {
    val tokenPair = AuthorizationManager().getAuthorizationTokens()
    println("""
        Access token = ${tokenPair.accessToken.accessToken}
        Seconds till expiration = ${tokenPair.accessToken.secondsTillExpiration()}
        Refresh token = ${tokenPair.refreshToken}
    """.trimIndent())

    FileTokenManager.writeRefreshTokenToFile(tokenPair.refreshToken)
    println(FileTokenManager.getRefreshTokenFromFile())
}