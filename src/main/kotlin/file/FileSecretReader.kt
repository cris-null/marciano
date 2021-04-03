package file

import java.io.File

object FileSecretReader {

    private const val SECRET_FILE_PATH = "secret.txt"

    private val tag = this.javaClass.simpleName

    fun getClientSecret(): String {
        val secretFile = File(SECRET_FILE_PATH)
        require(secretFile.exists()) { Logger.log(tag, "There's no file containing the client secret.") }

        return secretFile.readText()
    }
}