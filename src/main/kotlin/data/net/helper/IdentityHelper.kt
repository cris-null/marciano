package data.net.helper

import data.net.ServiceBuilder
import data.net.api.IdentityService
import data.net.model.Identity
import okhttp3.ResponseBody
import retrofit2.Response

object IdentityHelper {

    private val identity: IdentityService by lazy {
        ServiceBuilder.buildService(IdentityService::class.java, isUsingOauth = true)
    }

    suspend fun getIdentity(authorization: String): Response<Identity> = identity.getIdentity(authorization)
    suspend fun getPreferences(authorization: String): Response<ResponseBody> = identity.getTrophies(authorization)
}
