package kr.co.rolling.moment.library.network.repository

import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import timber.log.Timber
import java.net.ConnectException

/**
 * Repository Interface
 */
interface Repository {
    /**
     * 에러 발생 시 Error 로 Wrapping
     *
     * @param exception 발생한 Exception 종류
     * @param message Exception Message
     * @return Error
     */
    fun makeException(exception: Throwable, message: String?): CustomError {
        Timber.d("makeException() called with: exception = [$exception], message = [$message]")

        return when (exception) {
            is ConnectException -> {
                CustomError(ErrorType.NO_NETWORK_EXCEPTION, message ?: "")
            }

            else -> {
                CustomError(ErrorType.EXCEPTION, message ?: "")
            }
        }
    }
}