package data.net.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object ServiceBuilder {

    // Required to enable Kotlin serialization with Retrofit
    private val contentType = "application/json".toMediaType()

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            // Required to enable Kotlin serialization with Retrofit
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    fun <T> buildService(baseUrl: String, service: Class<T>): T {
        return buildRetrofit(baseUrl).create(service)
    }
}