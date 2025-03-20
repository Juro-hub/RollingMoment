package kr.co.rolling.moment.library.network

/**
 * Network 관련 상수 모음
 */
object NetworkConstants {
    //TODO 변경 필요
    var BASE_URL: String = "https://www.naver.com"

    /** API_CONNECT_TIMEOUT Timeout 3초 */
    const val API_CONNECT_TIMEOUT: Long = 3

    /** API_READ_TIMEOUT Timeout 10초 */
    const val API_READ_TIMEOUT: Long = 10

    /** API_WRITE_TIMEOUT Timeout 10초 */
    const val API_WRITE_TIMEOUT: Long = 10


    /**
     * 통신 URL 모음
     */
    const val API_PATH_SIGN_UP = "v1/auth/signup"
}