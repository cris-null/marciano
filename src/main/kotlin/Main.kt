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

fun main() = runBlocking {
    AccessTokenExpirationWatchdog.checkAccessTokenExpiration(3600)
}