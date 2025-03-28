package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 회원가입 Request DTO
 */
@Parcelize
data class RequestSignUp(
    /** 사용자 아이디 */
    @SerializedName("userId")
    val userId: String,

    /** 비밀번호 */
    @SerializedName("password")
    val password: String,

    /** 성별 */
    @SerializedName("gender")
    val gender: String,

    /** 별명 */
    @SerializedName("nickName")
    val nickName: String,
): Parcelable
