package file

import com.beust.klaxon.Klaxon
import net.AccessToken
import net.TokenResponseJsonRenamer
import java.io.File

object FileTokenManager {

    private const val REFRESH_TOKEN_FILEPATH = "refresh_token.txt"
    private val TAG = "${FileTokenManager::class.simpleName}"

    fun saveAccessTokenToFile(accessToken: AccessToken) {
        val refreshTokenFile = File(REFRESH_TOKEN_FILEPATH)
        if (refreshTokenFile.exists()) require(refreshTokenFile.canWrite())

        // Takes the refresh token response and converts it to a JSON string, using the proper renamer so that the string uses
        // underscores as it's a Reddit convention.
        val jsonString = Klaxon().fieldRenamer(TokenResponseJsonRenamer).toJsonString(accessToken).toPrettyJson()
        println("${TAG}: Writing refresh token to file.")
        File(REFRESH_TOKEN_FILEPATH).writeText(jsonString)
        println("${TAG}: OK")
    }

    fun getAccessTokenFromFile(): AccessToken {
        val refreshTokenFile = File(REFRESH_TOKEN_FILEPATH)
        require(refreshTokenFile.exists()) { "${TAG}: No refresh tokens have been saved to a file yet." }

        println("${TAG}: Reading refresh token from file.")
        val fileText = File(REFRESH_TOKEN_FILEPATH).readText()

        val accessToken = Klaxon().fieldRenamer(TokenResponseJsonRenamer).parse<AccessToken>(fileText)
        checkNotNull(accessToken)
        return accessToken
    }
}