package data.net.api

import data.net.model.Identity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers


interface IdentityService {

    @Headers(
        "User-Agent: marciano Kotlin/1.4"
    )
    @GET("/api/v1/me")
    suspend fun getIdentity(@Header("Authorization") authorization: String): Response<Identity>
}