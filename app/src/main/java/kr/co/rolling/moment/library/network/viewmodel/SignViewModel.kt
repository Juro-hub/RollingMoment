package kr.co.rolling.moment.library.network.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.rolling.moment.feature.base.BaseViewModel
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.request.RequestLogin
import kr.co.rolling.moment.library.network.data.request.RequestSignUp
import kr.co.rolling.moment.library.network.data.request.RequestSnsLogin
import kr.co.rolling.moment.library.network.data.request.RequestSplash
import kr.co.rolling.moment.library.network.data.response.SignUpResponse
import kr.co.rolling.moment.library.network.data.response.SnsLoginInfo
import kr.co.rolling.moment.library.network.data.response.TokenInfo
import kr.co.rolling.moment.library.network.data.response.toEntity
import kr.co.rolling.moment.library.network.repository.SignRepository
import kr.co.rolling.moment.library.network.util.SingleEvent
import javax.inject.Inject

/**
 * Intro ~ Sign 시 사용되는 ViewModel
 */
@HiltViewModel
class SignViewModel @Inject constructor(private val repository: SignRepository) : BaseViewModel() {
    /** Loading Live Data */
    var isLoading: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    var error: MutableLiveData<SingleEvent<CustomError>> = MutableLiveData()
        private set

    var signUpInfo: MutableLiveData<SingleEvent<SignUpResponse>> = MutableLiveData()
        private set

    var tokenInfo: MutableLiveData<SingleEvent<TokenInfo>> = MutableLiveData()
        private set

    var snsLoginInfo: MutableLiveData<SingleEvent<SnsLoginInfo>> = MutableLiveData()
        private set

    /**
     * Splash 요청
     */
    fun requestSplash(data: RequestSplash) {
        viewModelScope.launch {
            repository.requestSplash(
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
                        tokenInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    /**
     * SNS 로그인 요청
     * @param data SNS 로그인 데이터
     */
    fun requestSnsLogin(data: RequestSnsLogin) {
        viewModelScope.launch {
            repository.requestSnsLogin(
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
                        snsLoginInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    /**
     * 이메일 로그인 시도
     * @param data 이메일 로그인 Request DTO
     */
    fun requestSignIn(data: RequestLogin) {
        viewModelScope.launch {
            repository.requestLogin(
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
                        tokenInfo.postValue(SingleEvent(it.body.toEntity()))
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }

    /**
     * 회원가입 요청
     * @param data 회원가입 데이터
     */
    fun requestSignUp(data: RequestSignUp) {
        viewModelScope.launch {
            repository.requestSignUp(
                requestSignUp = data,
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
                        //TODO
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.meta.resCode, it.meta.resMessage)))
                    }
                }
            }
        }
    }
}