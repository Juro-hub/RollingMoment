package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 회원가입 완료 DTO
 */
@Parcelize
data class ResponseSignUp(
    @SerializedName("authToken")
    val authToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
) : Parcelable
