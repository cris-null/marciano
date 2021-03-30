package file

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.lang.StringBuilder

/**
 * Making pretty JSON files with Klaxon is convoluted. This extension will make it easier.
 *
 * Assumes that it is a valid JSON string.
 */
fun String.toPrettyJson(): String {
    val sb = StringBuilder(this)
    return (Parser.default().parse(sb) as JsonObject).toJsonString(true)
}