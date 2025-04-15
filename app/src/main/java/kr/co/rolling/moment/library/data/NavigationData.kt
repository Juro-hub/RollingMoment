package kr.co.rolling.moment.library.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.network.NetworkConstants

/**
 * 화면 이동 Data Class
 */
@Parcelize
data class NavigationData(
    val pageType: NetworkConstants.PageType,

    val navigateType: NetworkConstants.NavigationType,

    val customData: Map<String, String>
): Parcelable