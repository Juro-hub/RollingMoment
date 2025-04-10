package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 로그인 요청 DTO
 */
@Parcelize
data class RequestLogin(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("password")
    val password: String
) : Parcelable
