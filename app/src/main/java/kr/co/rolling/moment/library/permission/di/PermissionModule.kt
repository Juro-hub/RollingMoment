package kr.co.rolling.moment.library.permission.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.rolling.moment.library.permission.PermissionManager
import kr.co.rolling.moment.library.permission.PermissionValues
import kr.co.rolling.moment.library.util.AndroidInfo


/**
 * Permission DI 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
object PermissionModule {

    @Provides
    fun providePermissionManager(
        @ApplicationContext context: Context,
        androidInfo: AndroidInfo,
        permissionValues: PermissionValues
    ): PermissionManager = PermissionManager(context, androidInfo, permissionValues)

    @Provides
    fun providePermissionValue(
        androidInfo: AndroidInfo
    ): PermissionValues = PermissionValues()
}