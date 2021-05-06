package net.helper

import net.api.HistoryService
import net.buildService
import net.model.Listing
import retrofit2.Response

private val history: HistoryService by lazy {
    buildService(HistoryService::class.java, isUsingOauth = true)
}

suspend fun fetchSaved(authorization: String, username: String): Response<Listing> =
    history.fetchSaved(authorization, username)

suspend fun fetchSaved(authorization: String, username: String, nextAnchor:String): Response<Listing> =
    history.fetchSaved(authorization, username, nextAnchor = nextAnchor)