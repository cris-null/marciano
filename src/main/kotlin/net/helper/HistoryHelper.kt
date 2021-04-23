package net.helper

import net.api.HistoryService
import net.api.IdentityService
import net.buildService
import net.model.Identity
import net.model.TrophyResponse
import okhttp3.ResponseBody
import retrofit2.Response

private val history: HistoryService by lazy {
    buildService(HistoryService::class.java, isUsingOauth = true)
}

suspend fun getSaved(authorization: String, username: String): Response<ResponseBody> = history.getSaved(authorization, username)