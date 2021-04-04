package data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitBuilder(baseUrl: String) {

    private val contentType = "application/json".toMediaType()
    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

    val identityService: IdentityService by lazy { retrofit.create(IdentityService::class.java) }
    val authorizationService: AuthorizationService by lazy { retrofit.create(AuthorizationService::class.java) }
}