package kr.co.rolling.moment.library.util.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.rolling.moment.library.util.AndroidInfo
import javax.inject.Singleton

/**
 * Util DI 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Provides
    @Singleton
    fun provideAndroidInfo(): AndroidInfo =
        AndroidInfo()
}