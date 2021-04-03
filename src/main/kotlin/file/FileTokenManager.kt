package file

import Logger
import com.squareup.moshi.Moshi
import data.model.AccessToken
import java.io.File

object FileTokenManager {

    private val TAG = javaClass.simpleName
    private const val ACCESS_TOKEN_FILEPATH = "access_token.txt"
    private const val INDENTATION = "    "

    fun saveAccessTokenToFile(accessToken: AccessToken) {
        val refreshTokenFile = File(ACCESS_TOKEN_FILEPATH)
        if (refreshTokenFile.exists()) require(refreshTokenFile.canWrite())

        val moshi = Moshi.Builder().build()
        // indent() is for making it a bit more readable.
        val jsonAdapter = moshi.adapter(AccessToken::class.java).indent(INDENTATION)
        val jsonString = jsonAdapter.toJson(accessToken)

        Logger.log(TAG, " Writing access token to disk...")
        File(ACCESS_TOKEN_FILEPATH).writeText(jsonString)
        println("${TAG}: OK")
    }

    fun getAccessTokenFromFile(): AccessToken {
        val refreshTokenFile = File(ACCESS_TOKEN_FILEPATH)
        require(refreshTokenFile.exists()) { "${TAG}: No refresh tokens have been saved to a file yet." }

        Logger.log(TAG, "Reading refresh token from file.")
        val fileText = File(ACCESS_TOKEN_FILEPATH).readText()

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(AccessToken::class.java)
        val accessToken = jsonAdapter.fromJson(fileText)
        checkNotNull(accessToken)
        return accessToken
    }
}