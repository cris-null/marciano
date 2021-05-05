package net.api

import net.genUserAgent
import net.model.Listing
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.cert.TrustAnchor

interface HistoryService {

    @GET("/user/{username}/saved")
    suspend fun fetchSaved(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Header("User-Agent") userAgent: String = genUserAgent(),
    ): Response<Listing>

    @GET("/user/{username}/saved")
    suspend fun fetchSaved(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Query("after") nextAnchor: String,
        @Header("User-Agent") userAgent: String = genUserAgent(),
    ): Response<Listing>
}