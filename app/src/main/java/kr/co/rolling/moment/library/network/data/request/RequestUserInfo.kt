package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 사용자 정보 변경 요청 DTO
 */
@Parcelize
data class RequestUserInfo(
    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("gender")
    val gender: String,
) : Parcelable
