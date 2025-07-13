package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.network.NetworkConstants.GenderType

/**
 *
 */
@Parcelize
data class ResponseUserInfo(
    @SerializedName("nickname")
    val nickname: String? = "",

    @SerializedName("userId")
    val userId: String? = "",

    @SerializedName("gender")
    val gender: String? = "",
) : Parcelable


@Parcelize
data class UserInfo(
    val nickname: String = "",
    val userId: String = "",
    val gender: GenderType = GenderType.NONE
) : Parcelable

fun ResponseUserInfo.toEntity() = UserInfo(
    nickname = nickname ?: "",
    userId = userId ?: "",
    gender = GenderType.getType(gender ?: "")
)