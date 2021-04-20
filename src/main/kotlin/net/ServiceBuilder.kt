package net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import constant.BASE_URL
import constant.OAUTH_URL
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

// Required to enable Kotlin serialization with Retrofit
private val contentType: MediaType = "application/json".toMediaType()
private val regularClient by lazy { buildRetrofit(BASE_URL) }
private val oauthClient by lazy { buildRetrofit(OAUTH_URL) }

/**
 * Builds a service to make requests. The type of Retrofit client to use depends on [isUsingOauth].
 *
 * This is done because requests that require a token must be send to a different base URL.
 *
 * @param service A Retrofit compatible interface that defines endpoints in the standard way.
 */
fun <T> buildService(service: Class<T>, isUsingOauth: Boolean): T {
    return if (isUsingOauth) oauthClient.create(service)
    else regularClient.create(service)
}

private fun buildRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        // Required to enable Kotlin serialization with Retrofit
        // Note that I'm using a custom JSON configuration, [jsonParser], that is more lenient
        .addConverterFactory(configuredJson.asConverterFactory(contentType))
        .build()
}