package kr.co.rolling.moment.library.network.data


data class CustomError(val errorType: ErrorType, val message: String)

enum class ErrorType(val errorCode: Int) {
    /** 성공 */
    SUCCESS(0),

    /** 예외처리되지 않은 실패 */
    UNDEFINED_FAIL(-1),

    /** 토큰 만료 */
    EXPIRED_TOKEN(-101),

    /**
     * App 내 자체 Exception
     */
    /** 인터넷 미연결 */
    NO_NETWORK_EXCEPTION(-200),

    /** Parsing 수행 등에 있어 Exception 발생 */
    EXCEPTION(-201),

    /** 통신 수행 중 오류 발생 */
    HTTP_EXCEPTION(-202);

    companion object {
        fun fromCode(code: Int): ErrorType {
            return entries.find { it.errorCode == code } ?: UNDEFINED_FAIL
        }
    }
}