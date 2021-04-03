package data.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitBuilder(baseUrl: String) {

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val identityService: ApiService by lazy { retrofit.create(ApiService::class.java) }
    val authorizationService: AuthorizationService by lazy { retrofit.create(AuthorizationService::class.java) }
}