package kr.co.rolling.moment.library.util

import android.os.Build
import javax.inject.Inject

/**
 * 안드로이드 기본 정보를 가져온다.
 */
class AndroidInfo @Inject constructor() {
    /**
     * @return Android 28 이상 여/부
     */
    fun hasP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    /**
     * @return Android 29 이상 여/부
     */
    fun hasQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * @return 30 이상 여/부
     */
    fun hasR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * @return 31 이상 여/부
     */
    fun hasS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    /**
     * @return 33 이상 여/부
     */
    fun hasTiramisu(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    /**
     * @return OS 획득
     */
    fun getOsVersion(): String {
        return Build.VERSION.RELEASE
    }
}