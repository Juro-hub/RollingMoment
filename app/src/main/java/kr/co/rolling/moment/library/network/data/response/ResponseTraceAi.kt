package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 */
@Parcelize
data class ResponseTraceAi(
    @SerializedName("content")
    val content: String
) : Parcelable

@Parcelize
data class TraceAiInfo(
    val content: String = ""
) : Parcelable

fun ResponseTraceAi.toEntity() = TraceAiInfo(
    content = this.content
)