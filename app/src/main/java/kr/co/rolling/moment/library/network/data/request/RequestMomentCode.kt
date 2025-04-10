package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 흔적 AI 추천받기
 */
@Parcelize
data class RequestMomentCode (
    @SerializedName("momentCode")
    val momentCode: String
) : Parcelable