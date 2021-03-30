import authorization.AuthorizationManager
import com.beust.klaxon.Klaxon
import file.FileTokenManager
import net.AccessTokenRetriever
import net.RefreshTokenResponse
import net.RefreshTokenResponseJsonRenamer

fun main() {
//    val token = getAuthToken()
//    printAuthTokenInfo(token)
//    saveTokenToFile(token)
//    printRefreshTokenFromFile()

//    println(FileTokenManager.getRefreshTokenFromFile()?.getSecondsTillExpiration())


    val currentToken = FileTokenManager.getRefreshTokenFromFile()
    checkNotNull(currentToken)
    printAuthTokenInfo(currentToken)
    println(currentToken)

    val newTokenResponse = AccessTokenRetriever.getRenewedAccessToken(currentToken.refreshToken)
    val newToken = Klaxon().fieldRenamer(RefreshTokenResponseJsonRenamer).parse<RefreshTokenResponse>(newTokenResponse)
    checkNotNull(newToken)
    printAuthTokenInfo(newToken)
    println(newToken)
    saveTokenToFile(newToken)
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
