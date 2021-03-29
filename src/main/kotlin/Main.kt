import authorization.AuthorizationManager
import authorization.AuthorizationTokenPair
import file.FileTokenManager

fun main() {
}

fun getAuthTokenPair(): AuthorizationTokenPair {
    return AuthorizationManager().getAuthorizationTokens()
}

fun printAuthTokensInfo(authorizationTokenPair: AuthorizationTokenPair) {
    println(
        """
        Access token = ${authorizationTokenPair.accessToken.value}
        Seconds till expiration = ${authorizationTokenPair.accessToken.secondsTillExpiration()}
        Refresh token = ${authorizationTokenPair.refreshTokenValue}
    """.trimIndent()
    )
}

fun saveTokens(authorizationTokenPair: AuthorizationTokenPair) {
    FileTokenManager.writeRefreshTokenToFile(authorizationTokenPair.refreshTokenValue)
    println(FileTokenManager.getRefreshTokenFromFile())
}