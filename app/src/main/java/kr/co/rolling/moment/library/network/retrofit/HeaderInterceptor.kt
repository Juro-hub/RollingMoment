package kr.co.rolling.moment.library.network.retrofit

import kr.co.rolling.moment.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 * Header 정보 관련 Okhttp Interceptor Custom Class
 */
class HeaderInterceptor @Inject constructor() : Interceptor {
    /**
     * Interceptor class for setting of the dynamic headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val version = BuildConfig.VERSION_NAME

        val request = original.newBuilder()
            .addHeader("AppVersion", version)

        return chain.proceed(request.build())
    }
}