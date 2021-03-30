package file

import com.beust.klaxon.Klaxon
import net.RefreshTokenResponse
import net.RefreshTokenResponseJsonRenamer
import java.io.File

object FileTokenManager {

    private const val REFRESH_TOKEN_FILEPATH = "refresh_token.txt"
    private val refreshTokenFile = File(REFRESH_TOKEN_FILEPATH)

    fun writeRefreshTokenToFile(refreshTokenResponse: RefreshTokenResponse) {
        require(refreshTokenFile.canWrite())

        // Takes the refresh token response and converts it to a JSON string, using the proper renamer so that the string uses
        // underscores as it's a Reddit convention.
        val jsonString = Klaxon().fieldRenamer(RefreshTokenResponseJsonRenamer).toJsonString(refreshTokenResponse)
        println("Writing refresh token to file...")
        File(REFRESH_TOKEN_FILEPATH).writeText(jsonString)
        println("OK")
    }

    fun getRefreshTokenFromFile(): RefreshTokenResponse? {
        require(refreshTokenFile.exists()) { "No refresh tokens have been saved to a file yet." }

        println("Reading refresh token from file...")
        val fileText = File(REFRESH_TOKEN_FILEPATH).readText()

        return Klaxon().fieldRenamer(RefreshTokenResponseJsonRenamer).parse<RefreshTokenResponse>(fileText)
    }
}