package kr.co.rolling.moment.library.network.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.rolling.moment.feature.base.BaseViewModel
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.request.RequestSignUp
import kr.co.rolling.moment.library.network.data.response.SignUpResponse
import kr.co.rolling.moment.library.network.repository.SignRepository
import kr.co.rolling.moment.library.network.util.SingleEvent
import javax.inject.Inject

/**
 * Intro ~ Sign 시 사용되는 ViewModel
 */
@HiltViewModel
class SignViewModel @Inject constructor(private val repository: SignRepository) : BaseViewModel() {
    /** Loading Live Data */
    var isLoadingLive: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
        private set

    var error: MutableLiveData<SingleEvent<CustomError>> = MutableLiveData()
        private set

    var signUpInfo: MutableLiveData<SingleEvent<SignUpResponse>> = MutableLiveData()
        private set

    fun requestSignUp(data: RequestSignUp) {
        viewModelScope.launch {
            repository.requestSignUp(
                requestSignUp = data,
                onStart = {
                    isLoadingLive.postValue(SingleEvent(true))
                },
                onError = {
                    error.postValue(SingleEvent(it))
                },
                onComplete = {
                    isLoadingLive.postValue(SingleEvent(false))
                }
            ).collect {
                when (it.resCode) {
                    ErrorType.SUCCESS.errorCode -> {
                        //TODO
                    }

                    else -> {
                        error.postValue(SingleEvent(getError(it.resCode, it.resMessage)))
                    }
                }
            }
        }
    }
}