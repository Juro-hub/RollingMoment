package kr.co.rolling.moment.feature

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.BuildConfig
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ActivityMomentBinding
import kr.co.rolling.moment.feature.base.BaseActivity
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.component.CommonDialogData
import kr.co.rolling.moment.ui.util.showDialog
import timber.log.Timber
import javax.inject.Inject

/**
 * Single Activity
 */
@AndroidEntryPoint
class MomentActivity : BaseActivity() {
    private lateinit var binding: ActivityMomentBinding
    private val signViewModel by viewModels<SignViewModel>()

    @Inject
    lateinit var application: MomentApplication

    override fun initViewBinding() {
        binding = ActivityMomentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeViewModel() {
        observeEvent(signViewModel.isLoadingLive, ::networkLoading)
        observeEvent(signViewModel.error, ::networkError)
    }

    private fun networkError(singleEvent: SingleEvent<CustomError>) {
        singleEvent.getContentIfNotHandled()?.let {
            when (it.errorType) {
                //TODO 분기 필요
                ErrorType.UNDEFINED_FAIL -> {
                    showDialog(CommonDialogData(it.message, positiveText = getString(R.string.confirm)))
                }

                else -> {
                    showDialog(CommonDialogData(it.message, positiveText = getString(R.string.confirm)))
                }
            }
        }
    }

    private fun networkLoading(singleEvent: SingleEvent<Boolean>) {
        singleEvent.getContentIfNotHandled()?.let { isLoading ->
            if (isLoading) {
                application.showLoading(this)
            } else {
                application.hideLoading(this.javaClass.name)
            }
        }
    }
}