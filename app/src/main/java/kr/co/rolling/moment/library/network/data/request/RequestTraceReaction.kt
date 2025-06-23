package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 흔적 리액션 변경
 */
@Parcelize
data class RequestTraceReaction(
    @SerializedName("traceCode")
    val traceCode: String,

    @SerializedName("reactionType")
    val reactionType: String = "heart"
) : Parcelable