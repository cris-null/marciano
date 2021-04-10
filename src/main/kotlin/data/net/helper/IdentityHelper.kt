package data.net.helper

import data.net.ServiceBuilder
import data.net.api.IdentityService
import data.net.model.Identity
import data.net.model.TrophyList
import retrofit2.Response

object IdentityHelper {

    private val identity: IdentityService by lazy {
        ServiceBuilder.buildService(IdentityService::class.java, isUsingOauth = true)
    }

    suspend fun getIdentity(authorization: String): Response<Identity> = identity.getIdentity(authorization)
    suspend fun getTrophies(authorization: String): Response<TrophyList> = identity.getTrophies(authorization)
}
