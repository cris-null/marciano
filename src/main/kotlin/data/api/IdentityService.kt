package data.api

import data.model.Identity
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface IdentityService {

    @GET("/api/v1/me")
    fun getIdentity(@Header("Authorization") authorization: String): Call<Identity>
}