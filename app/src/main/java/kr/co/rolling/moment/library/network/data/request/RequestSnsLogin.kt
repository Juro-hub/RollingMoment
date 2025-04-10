package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * SNS 로그인 시 사용되는 정보
 */
@Parcelize
data class RequestSnsLogin (
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("type")
    val type: String
):Parcelable