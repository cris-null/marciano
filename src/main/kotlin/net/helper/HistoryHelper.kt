package net.helper

import net.api.HistoryService
import net.buildService
import net.model.Listing
import okhttp3.ResponseBody
import retrofit2.Response

private val history: HistoryService by lazy {
    buildService(HistoryService::class.java, isUsingOauth = true)
}

suspend fun getSaved(authorization: String, username: String): Response<Listing> =
    history.getSaved(authorization, username)