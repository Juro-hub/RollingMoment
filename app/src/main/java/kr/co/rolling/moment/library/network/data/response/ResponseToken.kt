package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseToken(
    @SerializedName("accessToken")
    val accessToken: String = "",

    @SerializedName("refreshToken")
    val refreshToken: String = "",

    @SerializedName("isUpdate")
    val isUpdate: Boolean = false,

    @SerializedName("message")
    val message: List<String>? = null
) : Parcelable

@Parcelize
data class SplashInfo(
    val accessToken: String = "",
    val refreshToken: String = "",
    val isUpdate: Boolean = false,
    val updateMessage: List<String>? = listOf<String>()
) : Parcelable

fun ResponseToken.toEntity(): SplashInfo {
    return SplashInfo(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        isUpdate = this.isUpdate,
        updateMessage = this.message
    )
}