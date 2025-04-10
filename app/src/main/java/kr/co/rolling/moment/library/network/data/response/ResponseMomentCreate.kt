package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 모먼트 생성 Response
 */
@Parcelize
data class ResponseMomentCreateResult(
    @SerializedName("momentCode")
    val momentCode: String
) : Parcelable

@Parcelize
data class MomentCreateResultInfo(
    val momentCode: String
) : Parcelable

fun ResponseMomentCreateResult.toEntity() = MomentCreateResultInfo(
    momentCode = this.momentCode
)