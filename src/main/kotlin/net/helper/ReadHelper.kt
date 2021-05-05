package net.helper

import net.api.ReadService
import net.buildService
import okhttp3.ResponseBody
import retrofit2.Response

private val read: ReadService by lazy {
    buildService(ReadService::class.java, isUsingOauth = true)
}

suspend fun fetchHotPics(authorization: String): Response<ResponseBody> = read.fetchHotPics(authorization)