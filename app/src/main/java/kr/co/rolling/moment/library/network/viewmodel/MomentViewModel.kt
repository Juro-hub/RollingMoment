package kr.co.rolling.moment.library.network.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kr.co.rolling.moment.feature.base.BaseViewModel
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.Constants.MOMENT_EDIT_SUCCESS
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.request.RequestMomentCode
import kr.co.rolling.moment.library.network.data.request.RequestMomentCreate
import kr.co.rolling.moment.library.network.data.request.RequestMomentEdit
import kr.co.rolling.moment.library.network.data.request.RequestMomentReport
import kr.co.rolling.moment.library.network.data.request.RequestTrace
import kr.co.rolling.moment.library.network.data.request.RequestTraceCode
import kr.co.rolling.moment.library.network.data.response.CreateTraceInfo
import kr.co.rolling.moment.library.network.data.response.MomentCreateInfo
import kr.co.rolling.moment.library.network.data.response.MomentCreateResultInfo
import kr.co.rolling.moment.library.network.data.response.MomentDetailInfo
import kr.co.rolling.moment.library.network.data.response.MomentEditInfo
import kr.co.rolling.moment.library.network.data.response.MomentEnrollInfo
import kr.co.rolling.moment.library.network.data.response.MomentListInfo
import kr.co.rolling.moment.library.network.data.response.MomentSimpleInfo
import kr.co.rolling.moment.library.network.data.response.MomentTraceInfo
import kr.co.rolling.moment.library.network.data.response.ReactionInfo
import kr.co.rolling.moment.library.network.data.response.TraceAiInfo
import kr.co.rolling.moment.library.network.data.response.toEntity
import kr.co.rolling.moment.library.network.repository.MomentRepository
import kr.co.rolling.moment.library.network.util.SingleEvent
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class MomentViewModel @Inject constructor(@ApplicationContext private val context: Context, private val repository: MomentRepository) : BaseViewModel() {
    var isLoading: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    var error: MutableLiveData<SingleEvent<CustomError>> = MutableLiveData()
        private set

    var momentCreateInfo: MutableLiveData<SingleEvent<MomentCreateInfo>> = MutableLiveData()
        private set

    var momentCode: MutableLiveData<SingleEvent<MomentCreateResultInfo>> = MutableLiveData()
        private set

    var traceAiData: MutableLiveData<SingleEvent<TraceAiInfo>> = MutableLiveData()
        private set

    var traceCreateInfo: MutableLiveData<SingleEvent<CreateTraceInfo>> = MutableLiveData()
        private set

    var momentDetailInfo: MutableLiveData<SingleEvent<MomentDetailInfo>> = MutableLiveData()
        private set

    var momentList: MutableLiveData<SingleEvent<MomentListInfo>> = MutableLiveData()
        private set

    var momentDelete: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    var momentTraceInfo: MutableLiveData<SingleEvent<MomentTraceInfo>> = MutableLiveData()
        private set

    var momentEnroll: MutableLiveData<SingleEvent<MomentEnrollInfo>> = MutableLiveData()
        private set

    var momentEditInfo: MutableLiveData<SingleEvent<MomentEditInfo>> = MutableLiveData()
        private set

    var momentSimpleInfo: MutableLiveData<SingleEvent<MomentSimpleInfo>> = MutableLiveData()
        private set

    var momentReportInfo: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    fun requestMomentCreateInfo() {
        viewModelScope.launch {
            repository.requestMomentCreateInfo(
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentCreateInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentCreate(data: RequestMomentCreate) {
        viewModelScope.launch {
            repository.requestMomentCreate(
                requestData = data,
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentCode.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun getCategoryInfo() = momentCreateInfo.value?.peekContent()?.categoryList

    fun getCoverImageInfo() = momentCreateInfo.value?.peekContent()?.coverImageList

    fun requestTraceAi(code: String) {
        viewModelScope.launch {
            repository.requestTraceAi(
                requestData = RequestMomentCode(
                    momentCode = code
                ),
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        traceAiData.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestTraceCreate(data: RequestTrace) {
        viewModelScope.launch {
            repository.requestTrace(
                requestData = data,
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        traceCreateInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentDetail(data: RequestMomentCode) {
        viewModelScope.launch {
            repository.requestMomentDetail(
                requestData = data,
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentDetailInfo.postValue(SingleEvent(it.body.toEntity(context = context)))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentList() {
        viewModelScope.launch {
            repository.requestMomentList(
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentList.postValue(SingleEvent(it.body.toEntity(context = context)))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentDelete(code: String) {
        viewModelScope.launch {
            repository.requestMomentDelete(
                requestData = RequestMomentCode(code),
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentDelete.postValue(SingleEvent(true))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentEnroll(code: String) {
        viewModelScope.launch {
            repository.requestMomentEnroll(
                requestData = RequestMomentCode(code),
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentEnroll.postValue(SingleEvent(it.body.toEntity(context)))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentEdit(requestData: RequestMomentEdit) {
        viewModelScope.launch {
            repository.requestMomentEdit(
                requestData = requestData,
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentCode.postValue(SingleEvent(MomentCreateResultInfo((MOMENT_EDIT_SUCCESS))))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentEditInfo(requestData: RequestMomentCode) {
        viewModelScope.launch {
            repository.requestMomentEditInfo(
                requestData = requestData,
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentEditInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentSimple(code: String) {
        viewModelScope.launch {
            repository.requestMomentSimple(
                requestData = RequestMomentCode(code),
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentSimpleInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestMomentReport(reportData: RequestMomentReport) {
        viewModelScope.launch {
            repository.requestMomentReport(
                requestData = reportData,
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentReportInfo.postValue(SingleEvent(true))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestTraceReaction(item: MomentTraceInfo) {
        viewModelScope.launch {
            repository.requestTraceReaction(
                requestData = RequestTraceCode(item.code),
                onStart = {
                    isLoading.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoading.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.meta.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        momentTraceInfo.postValue(
                            SingleEvent(
                                MomentTraceInfo(
                                    code = item.code,
                                    nickname = item.nickname,
                                    content = item.content,
                                    font = item.font,
                                    color = item.color,
                                    alignment = item.alignment,
                                    date = item.date,
                                    textColor = item.textColor,
                                    reactions = item.reactions?.map { reactions ->
                                        ReactionInfo(
                                            count = if (reactions.isClicked) {
                                                reactions.count - 1
                                            } else {
                                                reactions.count + 1
                                            },
                                            isClicked = !reactions.isClicked
                                        )
                                    }
                                )
                            ))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun getMomentInfo() = momentDetailInfo.value?.peekContent()
    fun isMomentExpired() = momentDetailInfo.value?.peekContent()?.isExpired == true
}