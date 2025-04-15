package kr.co.rolling.moment.library.network.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.data.NavigationData
import kr.co.rolling.moment.library.network.NetworkConstants

@Parcelize
data class ResponsePushList(
    @SerializedName("pushList")
    val pushList: List<ResponsePushItem>
) : Parcelable

@Parcelize
data class ResponsePushItem(
    @SerializedName("type")
    val type: String,  // 예: enum으로 관리 가능

    @SerializedName("content")
    val content: String,

    @SerializedName("date")
    val date: String, // yyyy.MM.dd HH:mm

    @SerializedName("pageType")
    val pageType: String,  // enum 가능

    @SerializedName("navigateType")
    val navigateType: String, // in / out

    @SerializedName("dataMap")
    val dataMap: Map<String, String>,

    @SerializedName("isRead")
    val isRead: Boolean // true = 읽음, false = 읽지 않음
) : Parcelable


@Parcelize
data class PushItem(
    val type: String,
    val content: String,
    val date: String,
    val navigateData: NavigationData,
    val isRead: Boolean
) : Parcelable

fun ResponsePushList.toEntity(): List<PushItem> {
    return this.pushList.map {
        PushItem(
            type = it.type,
            content = it.content,
            date = it.date,
            navigateData = NavigationData(
                pageType = NetworkConstants.PageType.getType(it.pageType),
                navigateType = NetworkConstants.NavigationType.getType(it.navigateType),
                customData = it.dataMap
            ),
            isRead = it.isRead
        )
    }
}

