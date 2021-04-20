package file

import net.configuredJson
import net.model.AccessToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

private const val ACCESS_TOKEN_FILEPATH = "access_token.txt"

fun loadToken(): AccessToken {
    val refreshTokenFile = File(ACCESS_TOKEN_FILEPATH)
    require(refreshTokenFile.exists()) { "No refresh tokens have been saved to a file yet." }

    println("Reading refresh token from file.")
    val fileText = File(ACCESS_TOKEN_FILEPATH).readText()

    return configuredJson.decodeFromString(fileText)
}

fun saveToken(accessToken: AccessToken) {
    val refreshTokenFile = File(ACCESS_TOKEN_FILEPATH)
    if (refreshTokenFile.exists()) require(refreshTokenFile.canWrite())

    val jsonString = configuredJson.encodeToString(accessToken)

    println("Saving token to file.")
    File(ACCESS_TOKEN_FILEPATH).writeText(jsonString)
    println("Token saved.")
}