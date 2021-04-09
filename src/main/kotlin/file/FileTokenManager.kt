package file

import Logger
import data.net.jsonParser
import data.net.model.AccessToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

object FileTokenManager {

    private val TAG = javaClass.simpleName
    private const val ACCESS_TOKEN_FILEPATH = "access_token.txt"

    fun saveAccessTokenToFile(accessToken: AccessToken) {
        val refreshTokenFile = File(ACCESS_TOKEN_FILEPATH)
        if (refreshTokenFile.exists()) require(refreshTokenFile.canWrite())

        val jsonString = jsonParser.encodeToString(accessToken)

        Logger.log(TAG, "Writing access token to disk...")
        File(ACCESS_TOKEN_FILEPATH).writeText(jsonString)
        println("${TAG}: OK")
    }

    fun getAccessTokenFromFile(): AccessToken {
        val refreshTokenFile = File(ACCESS_TOKEN_FILEPATH)
        require(refreshTokenFile.exists()) { Logger.log(TAG, "No refresh tokens have been saved to a file yet.") }

        Logger.log(TAG, "Reading refresh token from file.")
        val fileText = File(ACCESS_TOKEN_FILEPATH).readText()

        return jsonParser.decodeFromString(fileText)
    }
}