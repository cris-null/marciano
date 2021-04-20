package net.api

import net.genUserAgent
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ReadService {

    @GET("/r/pics/hot")
    suspend fun fetchHotPics(
        @Header("Authorization") authorization: String,
        @Header("User-Agent") userAgent: String = genUserAgent(),
    ): Response<ResponseBody>
}