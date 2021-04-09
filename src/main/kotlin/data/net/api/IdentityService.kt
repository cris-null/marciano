package data.net.api

import data.net.model.Identity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


interface IdentityService {

    @GET("/api/v1/me")
    fun getIdentity(@Header("Authorization") authorization: String): Call<Identity>
}