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
import kr.co.rolling.moment.library.network.di.DefaultApiService
import javax.inject.Inject

/**
 *
 */
class MainRepository @Inject constructor() : Repository {
    @Inject
    @DefaultApiService
    lateinit var apiService: ApiService

    @WorkerThread
    fun requestHomeInfo(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestHomeInfo()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMyPageInfo(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMyPageInfo()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestPushStatusChange(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestPushStatusChange()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestPushList(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestPushInfo()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestWithDraw(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestWithDraw()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestLogOut(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestLogOut()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}