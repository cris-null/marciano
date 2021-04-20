package file

import java.io.File

private const val SECRET_FILE_PATH = "secret.txt"

fun loadClientSecret(): String {
    val secretFile = File(SECRET_FILE_PATH)
    require(secretFile.exists()) { "There's no file containing the client secret." }

    return secretFile.readText()
}