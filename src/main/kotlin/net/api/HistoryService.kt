package net.api

import net.genUserAgent
import net.model.Listing
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HistoryService {

    @GET("/user/{username}/saved")
    suspend fun getSaved(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Header("User-Agent") userAgent: String = genUserAgent(),
    ): Response<Listing>
}