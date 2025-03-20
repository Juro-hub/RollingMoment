package kr.co.rolling.moment.library.network.data.response

import com.google.gson.annotations.SerializedName
import kr.co.rolling.moment.library.network.data.ErrorType

/**
 * Response Base 양식
 */
open class BaseResponseData {
    @SerializedName("resCd")
    val resCode: Int = ErrorType.UNDEFINED_FAIL.errorCode

    @SerializedName("resMsg")
    val resMessage: String = ""
}

/** 회원가입 완료 DTO */
data class SignUpResponse(
    val data :ResponseSignUp
):BaseResponseData()