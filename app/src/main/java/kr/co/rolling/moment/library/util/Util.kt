package kr.co.rolling.moment.library.util

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

/**
 * Bundle 의 직렬화를 수행
 *
 * @param T Parcelable 타입
 * @param key Key
 * @param clazz Class
 * @return Parcelable 객체
 */
fun <T : Parcelable> Bundle?.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return this?.let {
        BundleCompat.getParcelable(this, key, clazz)
    }
}