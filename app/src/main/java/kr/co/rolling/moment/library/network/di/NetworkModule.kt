package kr.co.rolling.moment.library.network.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.rolling.moment.library.network.ApiService
import kr.co.rolling.moment.library.network.NetworkConstants
import kr.co.rolling.moment.library.network.retrofit.HeaderInterceptor
import kr.co.rolling.moment.library.network.retrofit.NetworkConnectionInterceptor
import kr.co.rolling.moment.library.util.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/** 기본 API 서비스 통신 */
@Qualifier
annotation class DefaultNetwork

@Qualifier
annotation class DefaultRetrofit

@Qualifier
annotation class DefaultApiService

/**
 * Network 관련 처리 DI
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context
    ): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        preferenceManager: PreferenceManager
    ): HeaderInterceptor {
        return HeaderInterceptor(preferenceManager)
    }

    @Provides
    fun provideHttpLoggingInterceptorLevel(): HttpLoggingInterceptor.Level {
        return HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (!message.startsWith("{") && !message.startsWith("[")) {
                Timber.tag("okhttp.OkHttpClient").d(message)
                return@Logger
            }
            try {
                // Timber 와 Gson setPrettyPrinting 를 이용해 json 을 보기 편하게 표시해준다.
                Timber.tag("okhttp.OkHttpClient")
                    .d(GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(message)))
            } catch (m: JsonSyntaxException) {
                Timber.tag("okhttp.OkHttpClient").d(message)
            }
        }).apply {
            level = logLevel
        }
    }

    @DefaultNetwork
    @Provides
    @Singleton
    fun provideDefaultTimeoutOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        headerInterceptor: HeaderInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NetworkConstants.API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.API_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.API_READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    @DefaultRetrofit
    fun provideDefaultRetrofit(
        @DefaultNetwork okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConstants.BASE_URL)
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    @DefaultApiService
    fun provideDefaultApiService(@DefaultRetrofit retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}