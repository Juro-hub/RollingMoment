package kr.co.rolling.moment.library.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Spanned
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import com.google.gson.Gson
import kr.co.rolling.moment.R
import kr.co.rolling.moment.feature.MomentActivity
import kr.co.rolling.moment.feature.base.BaseActivity
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.data.NavigationData
import kr.co.rolling.moment.library.network.NetworkConstants

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

fun BaseFragment.navigate(navigateInfo: NavigationData) {
    (requireActivity() as MomentActivity).navigate(navigateInfo)
}

fun AppCompatActivity.navigate(navigateInfo: NavigationData) {
    when (navigateInfo.navigateType) {
        NetworkConstants.NavigationType.IN_APP -> {
            when (navigateInfo.pageType) {
                NetworkConstants.PageType.MOMENT -> {
                    val momentCode = navigateInfo.customData.momentCode
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.MomentDetailFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to momentCode))
                }
            }
        }

        NetworkConstants.NavigationType.OUT_LINK -> {
            //TODO MoveUrl 추가 필요
        }
    }
}

fun String.decodeUnicodeString(): String {
    return Gson().fromJson("\"$this\"", String::class.java)
}

fun BaseFragment.showToast(msg:String){
    Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
}

fun Context.moveToMarket(){
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = ("market://details?id=" + this@moveToMarket.packageName).toUri()
        setPackage("com.android.vending") // Play Store 앱에서 열도록 설정
    }
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    } else {
        // Play Store 앱이 없으면 웹으로 이동
        val webIntent = Intent(Intent.ACTION_VIEW,
            ("https://play.google.com/store/apps/details?id" + this.packageName).toUri())
        this.startActivity(webIntent)
    }
}