import authorization.AuthorizationManager
import file.FileTokenManager
import net.RefreshTokenResponse

fun main() {
    val token = getAuthToken()
    printAuthTokenInfo(token)
    saveTokenToFile(token)
    printRefreshTokenFromFile()
}


fun getAuthToken(): RefreshTokenResponse = AuthorizationManager().authorize()

fun printAuthTokenInfo(refreshTokenResponse: RefreshTokenResponse) {
    println(
        """
        Access token = ${refreshTokenResponse.accessToken}
        Seconds till expiration = ${refreshTokenResponse.getSecondsTillExpiration()}
        Refresh token = ${refreshTokenResponse.refreshToken}
    """.trimIndent()
    )
}

fun printAuthTokenInfoFromFile() = FileTokenManager.getRefreshTokenFromFile()?.let { printAuthTokenInfo(it) }

fun saveTokenToFile(refreshTokenResponse: RefreshTokenResponse) = FileTokenManager.writeRefreshTokenToFile(refreshTokenResponse)

fun printRefreshTokenFromFile() = println(FileTokenManager.getRefreshTokenFromFile())
