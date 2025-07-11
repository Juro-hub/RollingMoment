package kr.co.rolling.moment.library.network.data.response

import com.google.gson.annotations.SerializedName
import kr.co.rolling.moment.library.network.data.ErrorType

/**
 * Response Base 양식
 */
open class BaseResponseData {
    @SerializedName("meta")
    val meta: MetaResponse = MetaResponse(ErrorType.UNDEFINED_FAIL.errorCode, "")

    val resMessage = meta.resMessage
}

data class MetaResponse(
    @SerializedName("code")
    val resCode: Int = ErrorType.UNDEFINED_FAIL.errorCode,

    @SerializedName("message")
    val resMessage: String = ""
)


/** Token DTO */
data class TokenResponse(
    val body: ResponseToken
) : BaseResponseData()

/** SNS Login DTO */
data class SnsLoginResponse(
    val body: ResponseSnsLogin
) : BaseResponseData()

/** 회원가입 완료 DTO */
data class SignUpResponse(
    val body: ResponseSignUp
) : BaseResponseData()

/** 모먼트 생성 정보 조회 DTO */
data class MomentCreateResponse(
    val body: ResponseMomentCreateInfo
) : BaseResponseData()

/** 모먼트 생성 결과 DTO */
data class MomentCreateResultResponse(
    val body: ResponseMomentCreateResult
) : BaseResponseData()

/** 홈 결과 DTO */
data class HomeResponse(
    val body: ResponseHome
) : BaseResponseData()

/** 마이 페이지 DTO */
data class MyPageResponse(
    val body: ResponseMyPage
) : BaseResponseData()

/** 알림함 조회 */
data class PushResponse(
    val body: ResponsePushList
) : BaseResponseData()

/** 흔적 AI 데이터 조회 */
data class TraceAiResponse(
    val body: ResponseTraceAi
) : BaseResponseData()

/** 흔적 작성 DTO */
data class TraceCreateResponse(
    val body: ResponseCreateTrace
) : BaseResponseData()

/** 모먼트 상세 DTO */
data class MomentDetailResponse(
    val body: ResponseMomentDetail
) : BaseResponseData()

/** 모먼트 목록 조회 DTO */
data class MomentListResponse(
    val body: ResponseMomentList
) : BaseResponseData()

/** 모먼트 삭제 DTO */
data class MomentDeleteResponse(
    val body: Boolean
) : BaseResponseData()

/** 모먼트 대문 DTO */
data class MomentEnrollResponse(
    val body: ResponseMomentEnroll
) : BaseResponseData()

/** 모먼트 수정 정보조회 DTO */
data class MomentEditInfoResponse(
    val body: ResponseMomentEditInfo
) : BaseResponseData()

/** 모먼트 간략 정보조회 DTO */
data class MomentSimpleInfoResponse(
    val body: ResponseMomentSimpleInfo
) : BaseResponseData()

/** 모먼트 검색 목록 조회 DTO */
data class MomentListSearchResponse(
    val body: ResponseMomentSearchList
) : BaseResponseData()