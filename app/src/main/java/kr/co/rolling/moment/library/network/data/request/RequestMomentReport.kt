package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 모먼트/흔적 신고
 */
@Parcelize
data class RequestMomentReport(
    @SerializedName("type")
    val type: String = "",

    @SerializedName("contentType")
    val contentType: String = "",

    @SerializedName("cause")
    val cause: String = "",

    @SerializedName("code")
    val code: String,
) : Parcelable