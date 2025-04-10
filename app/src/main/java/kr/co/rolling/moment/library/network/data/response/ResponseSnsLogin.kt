package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ResponseSnsLogin(
    @SerializedName("id")
    val id: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
) : Parcelable

@Parcelize
data class SnsLoginInfo(
    val userId: String? = "",

    val nickname: String? = "",

    val accessToken: String = "",

    val refreshToken: String = ""
) : Parcelable

fun ResponseSnsLogin.toEntity() = SnsLoginInfo(
    userId = id,
    nickname = nickname,
    accessToken = accessToken,
    refreshToken = refreshToken
)