package kr.co.rolling.moment.library.network

import android.content.Context
import androidx.annotation.StringRes
import kr.co.rolling.moment.R

/**
 * Network 관련 상수 모음
 */
object NetworkConstants {
    var BASE_URL: String = "https://rollinmoment.n-e.kr"

    /** API_CONNECT_TIMEOUT Timeout 3초 */
    const val API_CONNECT_TIMEOUT: Long = 10

    /** API_READ_TIMEOUT Timeout 10초 */
    const val API_READ_TIMEOUT: Long = 10

    /** API_WRITE_TIMEOUT Timeout 10초 */
    const val API_WRITE_TIMEOUT: Long = 10

    /**
     * Header 관련
     */
    const val NETWORK_HEADER_KEY_BUILD_VERSION = "BuildVersion"
    const val NETWORK_HEADER_KEY_CONTENT_TYPE = "Content-Type"
    const val NETWORK_HEADER_KEY_DEVICE_TYPE = "DeviceType"
    const val NETWORK_HEADER_KEY_DEVICE_MODEL = "DeviceModel"
    const val NETWORK_HEADER_KEY_DEVICE_ID = "deviceId"
    const val NETWORK_HEADER_KEY_APP_VERSION = "AppVersion"
    const val NETWORK_HEADER_VALUE_DEVICE_TYPE = "Android"
    const val NETWORK_HEADER_KEY_REFRESH_TOKEN = "RefreshToken"
    const val NETWORK_HEADER_KEY_AUTH_TOKEN = "AuthToken"

    /**
     * 통신 URL 모음
     */
    // 회원가입
    const val API_PATH_SIGN_UP = "api/v1/auth/signup"

    // 스플래시
    const val API_SPLASH = "api/v1/splash"

    // SNS 로그인
    const val API_SNS_LOGIN = "api/v1/auth/sns"

    // 이메일 로그인
    const val API_EMAIL_LOGIN = "api/v1/auth/signin"

    // 모먼트 생성 화면 정보 조회
    const val API_MOMENT_CREATE_INFO = "api/v1/moment/create"

    // 모먼트 생성 요청
    const val API_MOMENT_CREATE = "api/v1/moment"

    // 홈 정보 요청
    const val API_HOME_INFO = "api/v1/home"

    // 내정보 조회 요청
    const val API_MY_INFO = "api/v1/mypage"

    // 알림 수신 여부 변경
    const val API_PUSH_STATUS_CHANGE = "api/v1/push/onoff"

    // 알림함 조회
    const val API_PUSH_INFO = "api/v1/push"

    // 흔적 AI 추천
    const val API_AI_RECOMMEND = "api/v1/trace/ai"

    // 흔적 작성
    const val API_TRACE_CREATE = "api/v1/trace"

    // 모먼트 상세 조회
    const val API_MOMENT_DETAIL = "api/v1/moment"

    // 탐색 정보 요청
    const val API_MOMENT_LIST = "api/v1/moment/list"

    // 모먼트 삭제 요청
    const val API_MOMENT_DELETE = "api/v1/moment/delete"

    // 흔적 리액션 변경
    const val API_TRACE_REACTION = "api/v1/reaction"

    // 로그아웃
    const val API_LOGOUT = "api/v1/auth/signout"

    // 회원 탈퇴
    const val API_WITHDRAW = "api/v1/auth/withdraw"

    // 모먼트 대문 조회
    const val API_MOMENT_ENROLL = "api/v1/moment/welcome"

    // 모먼트 수정
    const val API_MOMENT_EDIT = "api/v1/moment/setting"

    /**
     * SNS 로그인 유형
     */
    enum class SnsLoginType(val type: String) {
        /** 네이버 로그인 */
        NAVER("네이버"),

        /** 카카오 로그인 */
        KAKAO("카카오")
    }

    enum class GenderType(val type: String, @StringRes val textId: Int) {
        /** 남자 */
        MALE("MALE", R.string.sex_male),

        /** 여자 */
        FEMALE("FEMALE", R.string.sex_female),

        /** 비공개 */
        NONE("NONE", R.string.sex_none);

        companion object {
            fun getGender(context: Context, type: String): GenderType {
                return entries.find {
                    context.getString(it.textId) == type
                } ?: NONE
            }
        }
    }

    enum class MomentExpireType(val type: String, @StringRes val textId: Int, val code: String) {
        SEVEN_DAYS_LATER("SEVEN_DAYS_LATER", R.string.moment_crate_deadline_7, "7"),
        FIFTEEN_DAYS_LATER("FIFTEEN_DAYS_LATER", R.string.moment_crate_deadline_15, "15"),
        THIRTY_DAYS_LATER("THIRTY_DAYS_LATER", R.string.moment_crate_deadline_30, "30");

        companion object {
            fun getExpireDate(context: Context, text: String): String {
                return entries.find {
                    context.getString(it.textId) == text
                }?.code ?: SEVEN_DAYS_LATER.code
            }
        }
    }

    enum class MomentCategory(val code: String, @StringRes val textId: Int) {
        WEDDING("we", R.string.moment_category_we),
        BIRTHDAY("bi", R.string.moment_category_bi),
        FIRST_BIRTHDAY("fi", R.string.moment_category_fi),
        GRADUATION("gr", R.string.moment_category_gr),
        FAREWELL("fa", R.string.moment_category_fa),
        CHRISTMAS("ch", R.string.moment_category_ch),
        TEACHERS_DAY("te", R.string.moment_category_te),
        APPRECIATION("ap", R.string.moment_category_ap),
        CHEERING("che", R.string.moment_category_che),
        CONFESSION("co", R.string.moment_category_co),
        PRAISE("pr", R.string.moment_category_pr),
        CELEBRATION("ce", R.string.moment_category_ce);

        companion object {
            fun getCategory(code: String): MomentCategory {
                return entries.find {
                    it.code == code
                } ?: WEDDING
            }
        }
    }
}