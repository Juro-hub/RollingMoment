package kr.co.rolling.moment.feature

import android.content.ClipData
import android.content.ClipboardManager
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ActivityMomentBinding
import kr.co.rolling.moment.feature.base.BaseActivity
import kr.co.rolling.moment.feature.main.MyInfoFragment
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_KEY_INVITE_CODE
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.response.TokenInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MainViewModel
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.util.PreferenceManager
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.component.CommonDialogData
import kr.co.rolling.moment.ui.util.showDialog
import javax.inject.Inject

/**
 * Single Activity
 */
@AndroidEntryPoint
class MomentActivity : BaseActivity() {
    private lateinit var binding: ActivityMomentBinding
    private val signViewModel by viewModels<SignViewModel>()
    private val momentViewModel by viewModels<MomentViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var application: MomentApplication

    override fun initViewBinding() {
        binding = ActivityMomentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeViewModel() {
        observeEvent(signViewModel.isLoading, ::networkLoading)
        observeEvent(momentViewModel.isLoading, ::networkLoading)
        observeEvent(mainViewModel.isLoading, ::networkLoading)
        observeEvent(signViewModel.error, ::networkError)
        observeEvent(momentViewModel.error, ::networkError)
        observeEvent(mainViewModel.error, ::networkError)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val manager = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
        manager?.let {
            val uri = (it.primaryClip?.getItemAt(0)?.text ?: "").toString().toUri()
            val momentCode = uri.getQueryParameter(NETWORK_KEY_INVITE_CODE)
            momentCode?.let { code ->
                preferenceManager.setMomentCode(code)
                val emptyClip = ClipData.newPlainText("", "")
                it.setPrimaryClip(emptyClip)
            }
        }
    }

    private fun networkError(singleEvent: SingleEvent<CustomError>) {
        singleEvent.getContentIfNotHandled()?.let {
            when (it.errorType) {
                ErrorType.EXPIRED_TOKEN -> {
                    showDialog(CommonDialogData(it.message, positiveText = getString(R.string.confirm)), positiveCallback = {
                        preferenceManager.setTokenInfo(TokenInfo("", ""))
                        findNavController(R.id.nav_host_fragment).navigate(R.id.IntroFragment, NavOptions.Builder().setPopUpTo<MyInfoFragment>(true).build())
                    })
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