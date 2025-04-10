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
import kr.co.rolling.moment.library.network.data.request.RequestMomentCode
import kr.co.rolling.moment.library.network.data.request.RequestMomentCreate
import kr.co.rolling.moment.library.network.data.request.RequestTrace
import kr.co.rolling.moment.library.network.data.request.RequestTraceCode
import kr.co.settlebank.sb010pay.library.network.di.DefaultApiService
import javax.inject.Inject

/**
 *
 */
class MomentRepository @Inject constructor() : Repository {
    @Inject
    @DefaultApiService
    lateinit var apiService: ApiService

    @WorkerThread
    fun requestMomentCreateInfo(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentCreateInfo()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentCreate(
        requestData: RequestMomentCreate,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentCreate(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestTraceAi(
        requestData: RequestMomentCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestTraceAi(requestData.momentCode)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestTrace(
        requestData: RequestTrace,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestTraceCreate(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentDetail(
        requestData: RequestMomentCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentDetail(requestData.momentCode)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentList(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentList()

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentDelete(
        requestData: RequestMomentCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentDelete(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentEnroll(
        requestData: RequestMomentCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentEnroll(requestData.momentCode)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestTraceReaction(
        requestData: RequestTraceCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestTraceReaction(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentEdit(
        requestData: RequestMomentCreate,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentEdit(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}