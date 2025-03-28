package kr.co.rolling.moment.feature.base

import androidx.lifecycle.ViewModel
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType

/**
 * Base ViewModel
 */
abstract class BaseViewModel : ViewModel() {

    fun getError(errorCode: Int, message: String): CustomError {
        val error = ErrorType.fromCode(errorCode)
        return CustomError(error, message)
    }
}