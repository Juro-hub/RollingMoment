package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 알림함 조회 데이터 클래스
 */
@Parcelize
data class ResponseNotificationList(
    @SerializedName("pushList")
    val pushList: List<ResponseNotificationInfo>
) : Parcelable

@Parcelize
data class ResponseNotificationInfo(
    @SerializedName("type")
    val type: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("pageType")
    val pageType: String,

    @SerializedName("navigationType")
    val navigationType: String,
) : Parcelable

@Parcelize
data class NotificationListInfo(
    val pushList: List<NotificationInfo>? = null
) : Parcelable

@Parcelize
data class NotificationInfo(
    val type: String = "",

    val content: String = "",

    val date: String = "",

    val pageType: String = "",

    val navigationType: String = "",
) : Parcelable