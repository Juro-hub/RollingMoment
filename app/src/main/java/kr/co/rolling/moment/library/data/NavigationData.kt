package kr.co.rolling.moment.library.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.network.NetworkConstants

/**
 * 화면 이동 Data Class
 */
@Parcelize
data class NavigationData(
    val pageType: NetworkConstants.PageType,

    val navigateType: NetworkConstants.NavigationType,

    val customData: CustomData
) : Parcelable

@Parcelize
data class CustomData(
    val momentCode: String = "",
    val traceCode: String = ""
) : Parcelable

@Parcelize
data class NavigationResponse(
    @SerializedName("pageType")
    val pageType: String = "",

    @SerializedName("navigateType")
    val navigateType: String = "",

    @SerializedName("dataMap")
    val dataMap: DataMapResponse,
) : Parcelable

@Parcelize
data class DataMapResponse(
    @SerializedName("trace")
    val trace: String = "",

    @SerializedName("moment")
    val moment: String = ""
) : Parcelable

fun NavigationResponse.toEntity() = NavigationData(
    pageType = NetworkConstants.PageType.getType(pageType),
    navigateType = NetworkConstants.NavigationType.getType(navigateType),
    customData = CustomData(
        traceCode = dataMap.trace,
        momentCode = dataMap.moment
    )
)

