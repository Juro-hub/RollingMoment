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
import kr.co.rolling.moment.library.network.data.request.RequestMomentEdit
import kr.co.rolling.moment.library.network.data.request.RequestMomentReport
import kr.co.rolling.moment.library.network.data.request.RequestTrace
import kr.co.rolling.moment.library.network.data.request.RequestTraceDelete
import kr.co.rolling.moment.library.network.data.request.RequestTraceEdit
import kr.co.rolling.moment.library.network.data.request.RequestTraceReaction
import kr.co.rolling.moment.library.network.di.DefaultApiService
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
    fun requestMomentListAdmin(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentListAdmin()

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
        requestData: RequestTraceReaction,
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
        requestData: RequestMomentEdit,
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

    @WorkerThread
    fun requestMomentEditInfo(
        requestData: RequestMomentCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentEditInfo(requestData.momentCode)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentSimple(
        requestData: RequestMomentCode,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentSimple(requestData.momentCode)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentReport(
        requestData: RequestMomentReport,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentReport(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestTraceDelete(
        requestData: RequestTraceDelete,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestTraceDelete(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestTraceEdit(
        requestData: RequestTraceEdit,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestTraceEdit(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun requestMomentSearch(
        requestData: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (CustomError) -> Unit
    ) = flow {
        val response = apiService.requestMomentSearch(requestData)

        response.suspendOnSuccess {
            emit(data)
        }.onError {
            onError(CustomError(ErrorType.HTTP_EXCEPTION, "${statusCode.code}"))
        }.onException {
            onError(makeException(exception, message))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}