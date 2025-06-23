package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 흔적 작성
 */
@Parcelize
data class RequestTraceDelete (
    @SerializedName("traceCode")
    val traceCode : String,
): Parcelable