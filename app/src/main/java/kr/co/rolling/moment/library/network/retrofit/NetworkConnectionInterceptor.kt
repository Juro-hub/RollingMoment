package kr.co.rolling.moment.library.network.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject


/**
 * 네트워크 연결 관련 Okhttp Interceptor Custom Class
 */
class NetworkConnectionInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw NoInternetConnectException(
                CustomError(
                    ErrorType.NO_NETWORK_EXCEPTION,
                    context.getString(R.string.no_internet_exception)
                )
            )
        }
        return chain.proceed(chain.request())
    }

    /**
     * 인터넷 연결 여/부 획득한다.
     *
     * @return 인터넷 연결 여/부
     */
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connectivityManager?.let { cm ->
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
            }
        }
        return result
    }
}

/**
 * 인터넷 연결 안된 경우 Custom Exception 처리
 */

class NoInternetConnectException(
    customError: CustomError
) : ConnectException(customError.message)
