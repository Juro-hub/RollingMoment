package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 흔적 작성
 */
@Parcelize
data class RequestTraceEdit (
    @SerializedName("traceCode")
    val traceCode : String,

    @SerializedName("content")
    val content : String,

    @SerializedName("bgColor")
    val bgColor : String,

    @SerializedName("fontType")
    val fontType : String,

    @SerializedName("fontAlign")
    val fontAlign : String,

    @SerializedName("isAnonymous")
    val isAnonymous: Boolean,

    @SerializedName("textColor")
    val textColor: String
): Parcelable