package kr.co.rolling.moment.library.network.repository

import androidx.annotation.WorkerThread
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kr.co.rolling.moment.library.network.ApiService
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.request.RequestLogin
import kr.co.rolling.moment.library.network.data.request.RequestSignUp
import kr.co.rolling.moment.library.network.data.request.RequestSnsLogin
import kr.co.rolling.moment.library.network.data.request.RequestSplash
import kr.co.settlebank.sb010pay.library.network.di.DefaultApiService
import javax.inject.Inject

/**
 * Sign Repository
 */
class SignRepository @Inject constructor() : Repository {
    @Inject
    @DefaultApiService
    lateinit var apiService: ApiService

    @WorkerThread
    fun requestSplash(
        requestData: RequestSplash,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestSplash(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestSnsLogin(
        requestData: RequestSnsLogin,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestSnsLogin(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestLogin(
        requestData: RequestLogin,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestLogin(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestSignUp(
        requestSignUp: RequestSignUp,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestSignUp(requestSignUp)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}