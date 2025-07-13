package kr.co.rolling.moment.library.network

import com.skydoves.sandwich.ApiResponse
import kr.co.rolling.moment.library.network.data.request.RequestLogin
import kr.co.rolling.moment.library.network.data.request.RequestMomentCode
import kr.co.rolling.moment.library.network.data.request.RequestMomentCreate
import kr.co.rolling.moment.library.network.data.request.RequestMomentEdit
import kr.co.rolling.moment.library.network.data.request.RequestMomentReport
import kr.co.rolling.moment.library.network.data.request.RequestSignUp
import kr.co.rolling.moment.library.network.data.request.RequestSnsLogin
import kr.co.rolling.moment.library.network.data.request.RequestSplash
import kr.co.rolling.moment.library.network.data.request.RequestTrace
import kr.co.rolling.moment.library.network.data.request.RequestTraceDelete
import kr.co.rolling.moment.library.network.data.request.RequestTraceEdit
import kr.co.rolling.moment.library.network.data.request.RequestTraceReaction
import kr.co.rolling.moment.library.network.data.request.RequestUserInfo
import kr.co.rolling.moment.library.network.data.response.BaseResponseData
import kr.co.rolling.moment.library.network.data.response.HomeResponse
import kr.co.rolling.moment.library.network.data.response.MomentCreateResponse
import kr.co.rolling.moment.library.network.data.response.MomentCreateResultResponse
import kr.co.rolling.moment.library.network.data.response.MomentDeleteResponse
import kr.co.rolling.moment.library.network.data.response.MomentDetailResponse
import kr.co.rolling.moment.library.network.data.response.MomentEditInfoResponse
import kr.co.rolling.moment.library.network.data.response.MomentEnrollResponse
import kr.co.rolling.moment.library.network.data.response.MomentListResponse
import kr.co.rolling.moment.library.network.data.response.MomentListSearchResponse
import kr.co.rolling.moment.library.network.data.response.MomentSimpleInfoResponse
import kr.co.rolling.moment.library.network.data.response.MyPageResponse
import kr.co.rolling.moment.library.network.data.response.PushResponse
import kr.co.rolling.moment.library.network.data.response.ResponseUserInfo
import kr.co.rolling.moment.library.network.data.response.SignUpResponse
import kr.co.rolling.moment.library.network.data.response.SnsLoginResponse
import kr.co.rolling.moment.library.network.data.response.TokenResponse
import kr.co.rolling.moment.library.network.data.response.TraceAiResponse
import kr.co.rolling.moment.library.network.data.response.TraceCreateResponse
import kr.co.rolling.moment.library.network.data.response.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 */
interface ApiService {
    // Splash
    @POST(NetworkConstants.API_SPLASH)
    suspend fun requestSplash(
        @Body requestData: RequestSplash
    ): ApiResponse<TokenResponse>

    // SNS 로그인
    @POST(NetworkConstants.API_SNS_LOGIN)
    suspend fun requestSnsLogin(
        @Body requestSnsLogin: RequestSnsLogin
    ): ApiResponse<SnsLoginResponse>

    // 이메일 로그인
    @POST(NetworkConstants.API_EMAIL_LOGIN)
    suspend fun requestLogin(
        @Body requestSnsLogin: RequestLogin
    ): ApiResponse<TokenResponse>

    // 회원가입
    @POST(NetworkConstants.API_PATH_SIGN_UP)
    suspend fun requestSignUp(
        @Body requestSignUp: RequestSignUp
    ): ApiResponse<SignUpResponse>

    // 모먼트 생성 페이지 정보 조회
    @GET(NetworkConstants.API_MOMENT_CREATE_INFO)
    suspend fun requestMomentCreateInfo(): ApiResponse<MomentCreateResponse>

    // 모먼트 생성
    @POST(NetworkConstants.API_MOMENT_CREATE)
    suspend fun requestMomentCreate(
        @Body requestData: RequestMomentCreate
    ): ApiResponse<MomentCreateResultResponse>

    // 홈 정보 조회
    @GET(NetworkConstants.API_HOME_INFO)
    suspend fun requestHomeInfo(): ApiResponse<HomeResponse>

    // 홈 정보 조회
    @GET(NetworkConstants.API_MY_INFO)
    suspend fun requestMyPageInfo(): ApiResponse<MyPageResponse>

    // 알림 변경
    @PATCH(NetworkConstants.API_PUSH_STATUS_CHANGE)
    suspend fun requestPushStatusChange(): ApiResponse<BaseResponseData>

    // 알림함 정보 조회
    @GET(NetworkConstants.API_PUSH_INFO)
    suspend fun requestPushInfo(): ApiResponse<PushResponse>

    // 흔적 AI 조회
    @GET(NetworkConstants.API_AI_RECOMMEND)
    suspend fun requestTraceAi(
        @Query("momentCode") momentCode: String
    ): ApiResponse<TraceAiResponse>

    // 흔적 AI 조회
    @POST(NetworkConstants.API_TRACE_CREATE)
    suspend fun requestTraceCreate(
        @Body requestTrace: RequestTrace
    ): ApiResponse<TraceCreateResponse>

    @GET(NetworkConstants.API_MOMENT_DETAIL)
    suspend fun requestMomentDetail(
        @Query("momentCode") momentCode: String
    ): ApiResponse<MomentDetailResponse>

    @GET(NetworkConstants.API_MOMENT_LIST)
    suspend fun requestMomentList(): ApiResponse<MomentListResponse>

    @PATCH(NetworkConstants.API_MOMENT_DELETE)
    suspend fun requestMomentDelete(
        @Body code: RequestMomentCode
    ): ApiResponse<MomentDeleteResponse>

    @GET(NetworkConstants.API_MOMENT_ENROLL)
    suspend fun requestMomentEnroll(
        @Query("momentCode") momentCode: String
    ): ApiResponse<MomentEnrollResponse>

    @POST(NetworkConstants.API_TRACE_REACTION)
    suspend fun requestTraceReaction(
        @Body code: RequestTraceReaction
    ): ApiResponse<MomentDeleteResponse>

    @POST(NetworkConstants.API_LOGOUT)
    suspend fun requestLogOut(
    ): ApiResponse<BaseResponseData>

    @PATCH(NetworkConstants.API_WITHDRAW)
    suspend fun requestWithDraw(
    ): ApiResponse<BaseResponseData>

    @PATCH(NetworkConstants.API_MOMENT_EDIT)
    suspend fun requestMomentEdit(
        @Body code: RequestMomentEdit
    ): ApiResponse<BaseResponseData>

    @GET(NetworkConstants.API_MOMENT_EDIT)
    suspend fun requestMomentEditInfo(
        @Query("momentCode") momentCode: String
    ): ApiResponse<MomentEditInfoResponse>

    @GET(NetworkConstants.API_MOMENT_SIMPLE)
    suspend fun requestMomentSimple(
        @Query("momentCode") momentCode: String
    ): ApiResponse<MomentSimpleInfoResponse>

    // 모먼트/흔적 신고
    @POST(NetworkConstants.API_MOMENT_REPORT)
    suspend fun requestMomentReport(
        @Body requestReport: RequestMomentReport
    ): ApiResponse<BaseResponseData>

    // 흔적 삭제
    @PATCH(NetworkConstants.API_TRACE_DELETE)
    suspend fun requestTraceDelete(
        @Body requestReport: RequestTraceDelete
    ): ApiResponse<BaseResponseData>

    // 흔적 수정
    @PATCH(NetworkConstants.API_TRACE_EDIT)
    suspend fun requestTraceEdit(
        @Body requestTrace: RequestTraceEdit
    ): ApiResponse<BaseResponseData>

    // 모먼트 조회
    @GET(NetworkConstants.API_MOMENT_SEARCH)
    suspend fun requestMomentSearch(
        @Query("searchText") requestSearch: String
    ): ApiResponse<MomentListSearchResponse>

    // 사용자 정보 조회
    @GET(NetworkConstants.API_USER_INFO)
    suspend fun requestUserInfo(): ApiResponse<UserInfoResponse>

    // 사용자 정보 수정
    @PATCH(NetworkConstants.API_USER_INFO_UPDATE)
    suspend fun requestUserInfoUpdate(
        @Body requestData : RequestUserInfo
    ): ApiResponse<BaseResponseData>

    @GET(NetworkConstants.API_ADMIN_MOMENT)
    suspend fun requestMomentListAdmin(): ApiResponse<MomentListResponse>
}