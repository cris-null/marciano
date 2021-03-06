package net.helper

import net.api.IdentityService
import net.buildService
import net.model.Identity
import net.model.TrophyResponse
import retrofit2.Response

private val identity: IdentityService by lazy {
    buildService(IdentityService::class.java, isUsingOauth = true)
}

suspend fun fetchIdentity(authorization: String): Response<Identity> = identity.fetchIdentity(authorization)
suspend fun fetchTrophies(authorization: String): Response<TrophyResponse> = identity.fetchTrophies(authorization)