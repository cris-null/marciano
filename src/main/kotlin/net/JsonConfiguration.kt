package net

import kotlinx.serialization.json.Json

/**
 * The Kotlinx serialization parser. Use it to serialize/deserialize
 * objects into/from JSON.
 *
 * Changed a few of it's parameters for ease of use:
 *
 * encodeDefaults = true
 *
 * prettyPrint = true
 *
 * ignoreUnknownKeys = true
 */
val configuredJson = Json {
    // Without this, class properties with default
    // will not be serialized
    encodeDefaults = true
    prettyPrint = true
    // Without this, each model class you have MUST implement
    // every key found in the JSON to parse
    ignoreUnknownKeys = true
}