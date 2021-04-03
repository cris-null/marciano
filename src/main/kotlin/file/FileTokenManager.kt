package file

import Logger
import com.squareup.moshi.Moshi
import data.model.AccessToken
import java.io.File

object FileTokenManager {

    private const val REFRESH_TOKEN_FILEPATH = "refresh_token.txt"
    private val tag = "${FileTokenManager::class.simpleName}"

    fun saveAccessTokenToFile(accessToken: AccessToken) {
        val refreshTokenFile = File(REFRESH_TOKEN_FILEPATH)
        if (refreshTokenFile.exists()) require(refreshTokenFile.canWrite())

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(AccessToken::class.java)
        val jsonString = jsonAdapter.toJson(accessToken)

        Logger.log(tag, " Writing refresh token to file.")
        File(REFRESH_TOKEN_FILEPATH).writeText(jsonString)
        println("${tag}: OK")
    }

    fun getAccessTokenFromFile(): AccessToken {
        val refreshTokenFile = File(REFRESH_TOKEN_FILEPATH)
        require(refreshTokenFile.exists()) { "${tag}: No refresh tokens have been saved to a file yet." }

        Logger.log(tag, "Reading refresh token from file.")
        val fileText = File(REFRESH_TOKEN_FILEPATH).readText()

        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(AccessToken::class.java)
        val accessToken = jsonAdapter.fromJson(fileText)
        checkNotNull(accessToken)
        return accessToken
    }
}