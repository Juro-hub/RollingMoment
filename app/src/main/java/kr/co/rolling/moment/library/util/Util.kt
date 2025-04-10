package kr.co.rolling.moment.library.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Spanned
import androidx.core.os.BundleCompat
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import kr.co.rolling.moment.feature.base.BaseActivity
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.NetworkConstants
import androidx.core.net.toUri

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

/**
 * Strings Xml 에서의 CData 속성 적용
 *
 * @return Spanned 처리 된 Text
 */
fun String.htmlToSpanned(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun BaseActivity.landingOutLink(link: String) {
    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
    } catch (e: ActivityNotFoundException) {
       e.message
    }
}