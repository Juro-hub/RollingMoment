package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 마이 페이지 정보 조회
 */
@Parcelize
data class ResponseMyPage(
    @SerializedName("nickName")
    val nickName: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("isAlarmOn")
    val isAlarmOn: Boolean,

    @SerializedName("serviceUrl")
    val serviceUrl: String,

    @SerializedName("policyUrl")
    val policyUrl: String
) : Parcelable

@Parcelize
data class MyPageInfo(
    val nickName: String = "",

    val userId: String = "",

    val isAlarmOn: Boolean = false,

    val serviceUrl: String = "",

    val policyUrl: String = ""
) : Parcelable

fun ResponseMyPage.toEntity() = MyPageInfo(
    nickName = this.nickName,
    userId = this.userId,
    isAlarmOn = this.isAlarmOn,
    serviceUrl = this.serviceUrl,
    policyUrl = this.policyUrl
)