package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseToken(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
) : Parcelable

@Parcelize
data class TokenInfo(
    val accessToken: String = "",
    val refreshToken: String = ""
) : Parcelable

fun ResponseToken.toEntity(): TokenInfo {
    return TokenInfo(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}