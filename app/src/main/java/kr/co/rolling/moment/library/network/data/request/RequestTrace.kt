package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 흔적 작성
 */
@Parcelize
data class RequestTrace (
    @SerializedName("momentCode")
    val momentCode : String,

    @SerializedName("content")
    val content : String,

    @SerializedName("bgColor")
    val bgColor : String,

    @SerializedName("fontType")
    val fontType : String,

    @SerializedName("fontAlias")
    val fontAlias : String,
): Parcelable