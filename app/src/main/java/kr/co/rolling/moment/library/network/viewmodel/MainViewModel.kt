package kr.co.rolling.moment.library.network.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kr.co.rolling.moment.feature.base.BaseViewModel
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.response.HomeInfo
import kr.co.rolling.moment.library.network.data.response.MyPageInfo
import kr.co.rolling.moment.library.network.data.response.PushItem
import kr.co.rolling.moment.library.network.data.response.toEntity
import kr.co.rolling.moment.library.network.repository.MainRepository
import kr.co.rolling.moment.library.network.util.SingleEvent
import javax.inject.Inject

/**
 * 메인 화면 (홈, 탐색, 알림, 내정보) 에서 사용되는 ViewModel 입니다.
 */
@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val context: Context, private val repository: MainRepository) : BaseViewModel() {
    var isLoading: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    var error: MutableLiveData<SingleEvent<CustomError>> = MutableLiveData()
        private set

    var homeInfo: MutableLiveData<SingleEvent<HomeInfo>> = MutableLiveData()
        private set

    var myPageInfo: MutableLiveData<SingleEvent<MyPageInfo>> = MutableLiveData()
        private set

    var pushInfo: MutableLiveData<SingleEvent<List<PushItem>>> = MutableLiveData()
        private set

    var isLogout: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    var isWithdraw: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    fun requestHomeInfo() {
        viewModelScope.launch {
            repository.requestHomeInfo(
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
                        homeInfo.postValue(SingleEvent(it.body.toEntity(context)))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun getExpiredMomentList() = homeInfo.value?.peekContent()?.expiredMoment
    fun getProgressMomentList() = homeInfo.value?.peekContent()?.progressMoment

    fun requestMyPageInfo() {
        viewModelScope.launch {
            repository.requestMyPageInfo(
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
                        myPageInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestPushStatusChange() {
        viewModelScope.launch {
            repository.requestPushStatusChange(
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
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestPushInfo() {
        viewModelScope.launch {
            repository.requestPushList(
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
                        pushInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestWithDraw() {
        viewModelScope.launch {
            repository.requestWithDraw(
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
                        isWithdraw.postValue(SingleEvent(true))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    fun requestLogout() {
        viewModelScope.launch {
            repository.requestLogOut(
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
                        isLogout.postValue(SingleEvent(true))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }
}