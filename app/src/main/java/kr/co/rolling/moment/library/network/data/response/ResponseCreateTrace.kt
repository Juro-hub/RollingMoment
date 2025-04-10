package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 */
@Parcelize
data class ResponseCreateTrace(
    @SerializedName("momentCode")
    val momentCode: String
) : Parcelable


@Parcelize
data class CreateTraceInfo(
    val momentCode: String = ""
) : Parcelable

fun ResponseCreateTrace.toEntity() = CreateTraceInfo(
    momentCode = this.momentCode
)