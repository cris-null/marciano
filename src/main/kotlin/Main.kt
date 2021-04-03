import authorization.AccessTokenExpirationWatchdog
import authorization.AccessTokenManager
import data.api.RetrofitBuilder
import data.model.Identity
import file.FileTokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun main() {
    AccessTokenManager.refreshAccessToken()
}
//
//fun test() = runBlocking {
//    launch {
//        AccessTokenExpirationWatchdog.monitorAccessTokenExpiration(1800, 30000)
//        delay(10000)
//    }
//
//    val identityService = RetrofitBuilder.identityService
//
//    val token = FileTokenManager.getAccessTokenFromFile().accessToken
//
//    val call = identityService.getIdentity("bearer $token")
//    call.enqueue(object : Callback<Identity> {
//        /**
//         * Invoked for a received HTTP response.
//         *
//         *
//         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
//         * Call [Response.isSuccessful] to determine if the response indicates success.
//         */
//        /**
//         * Invoked for a received HTTP response.
//         *
//         *
//         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
//         * Call [Response.isSuccessful] to determine if the response indicates success.
//         */
//        override fun onResponse(call: Call<Identity>, response: Response<Identity>) {
//            println(response.body())
//        }
//
//        /**
//         * Invoked when a network exception occurred talking to the server or when an unexpected exception
//         * occurred creating the request or processing the response.
//         */
//        /**
//         * Invoked when a network exception occurred talking to the server or when an unexpected exception
//         * occurred creating the request or processing the response.
//         */
//        override fun onFailure(call: Call<Identity>, t: Throwable) {
//            println("did not work")
//        }
//
//    })
//}