package file

import java.io.File

object FileTokenManager {

    private const val REFRESH_TOKEN_FILE = "refresh_token.txt"

    fun writeRefreshTokenToFile(token: String) {
        File(REFRESH_TOKEN_FILE).writeText(token)
    }

    fun getRefreshTokenFromFile(): String {
        // It's fine to use readText() here since the file will be very small.
        return File(REFRESH_TOKEN_FILE).readText()
    }
}