package file

import java.io.File

object FileTokenManager {

    private const val REFRESH_TOKEN_FILE = "refresh_token.txt"

    fun writeRefreshTokenToFile(token: String) {
        println("Writing refresh token to file...")
        File(REFRESH_TOKEN_FILE).writeText(token)
        println("OK")
    }

    fun getRefreshTokenFromFile(): String {
        println("Reading refresh token from file...")
        // It's fine to use readText() here since the file will be very small.
        return File(REFRESH_TOKEN_FILE).readText()
    }
}