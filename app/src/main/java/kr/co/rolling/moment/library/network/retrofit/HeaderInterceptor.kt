package kr.co.rolling.moment.library.network.retrofit

import android.os.Build
import kr.co.rolling.moment.BuildConfig
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_APP_VERSION
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_AUTH_TOKEN
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_BUILD_VERSION
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_CONTENT_TYPE
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_DEVICE_ID
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_DEVICE_MODEL
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_DEVICE_TYPE
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_KEY_REFRESH_TOKEN
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_HEADER_VALUE_DEVICE_TYPE
import kr.co.rolling.moment.library.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 * Header 정보 관련 Okhttp Interceptor Custom Class
 */
class HeaderInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager
) : Interceptor {
    /**
     * Interceptor class for setting of the dynamic headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val request = original.newBuilder()
            .addHeader(NETWORK_HEADER_KEY_REFRESH_TOKEN, preferenceManager.getRefreshToken())
            .addHeader(NETWORK_HEADER_KEY_AUTH_TOKEN, preferenceManager.getAccessToken())
            .addHeader(NETWORK_HEADER_KEY_BUILD_VERSION, BuildConfig.VERSION_NAME)
            .addHeader(NETWORK_HEADER_KEY_CONTENT_TYPE, "application/json")
            .addHeader(NETWORK_HEADER_KEY_DEVICE_TYPE, NETWORK_HEADER_VALUE_DEVICE_TYPE)
            .addHeader(NETWORK_HEADER_KEY_DEVICE_MODEL, Build.MODEL)
            .addHeader(NETWORK_HEADER_KEY_DEVICE_ID, preferenceManager.getUserId())
            .addHeader(NETWORK_HEADER_KEY_APP_VERSION, BuildConfig.VERSION_CODE.toString())
        return chain.proceed(request.build())
    }
}