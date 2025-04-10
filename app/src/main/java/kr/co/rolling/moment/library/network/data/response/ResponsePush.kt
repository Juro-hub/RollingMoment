package kr.co.rolling.moment.library.network.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
    val pageType: String,
    val navigateType: String,
    val dataMap: Map<String, String>,
    val isRead: Boolean
) : Parcelable

fun ResponsePushList.toEntity(): List<PushItem> {
    return this.pushList.map {
        PushItem(
            type = it.type,
            content = it.content,
            date = it.date,
            pageType = it.pageType,
            navigateType = it.navigateType,
            dataMap = it.dataMap,
            isRead = it.isRead
        )
    }
}

