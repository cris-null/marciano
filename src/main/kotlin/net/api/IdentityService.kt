package net.api

import net.model.Identity
import net.model.TrophyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

/**
 * A representation of the endpoints related to the "identity" scope.
 */
interface IdentityService {

    @Headers(
        "User-Agent: marciano Kotlin/1.4"
    )
    @GET("/api/v1/me")
    suspend fun getIdentity(@Header("Authorization") authorization: String): Response<Identity>

    @Headers(
        "User-Agent: marciano Kotlin/1.4"
    )
    @GET("/api/v1/me/trophies")
    suspend fun getTrophies(@Header("Authorization") authorization: String): Response<TrophyResponse>
}