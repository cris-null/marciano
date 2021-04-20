package net.api

import net.genUserAgent
import net.model.Identity
import net.model.TrophyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * A representation of the endpoints related to the "identity" scope.
 */
interface IdentityService {

    @GET("/api/v1/me")
    suspend fun getIdentity(
        @Header("Authorization") authorization: String,
        @Header("User-Agent") userAgent: String = genUserAgent(),
    ): Response<Identity>

    @GET("/api/v1/me/trophies")
    suspend fun getTrophies(
        @Header("Authorization") authorization: String,
        @Header("User-Agent") userAgent: String = genUserAgent(),
    ): Response<TrophyResponse>
}