package kr.co.rolling.moment.library.util

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

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

/**
 * Fragment Navigation 간 Exception 최소화를 하며, 중복 되어 이동하지 않도록 설정한다.
 *
 * @param navDirections Navigate Direction 정보
 * @param navOptions Navigation Option
 * @param navExtras Extras
 */
fun NavController.navigateSafe(
    navDirections: NavDirections,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(navDirections.actionId) ?: graph.getAction(navDirections.actionId)

    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(navDirections.actionId, navDirections.arguments, navOptions, navExtras)
    }
}