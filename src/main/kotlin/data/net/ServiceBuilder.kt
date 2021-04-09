package data.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import constant.RegisteredAppInformation
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

object ServiceBuilder {

    // Required to enable Kotlin serialization with Retrofit
    private val contentType = "application/json".toMediaType()

    private val regularClient by lazy { buildRetrofit(RegisteredAppInformation.BASE_URL) }
    private val oauthClient by lazy { buildRetrofit(RegisteredAppInformation.OAUTH_URL) }

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            // Required to enable Kotlin serialization with Retrofit
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

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
}