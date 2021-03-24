package util.string

object RandomStringGenerator {

    fun getRandomString(): String {
        return java.util.UUID.randomUUID().toString()
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}