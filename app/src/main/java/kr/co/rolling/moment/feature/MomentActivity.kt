package kr.co.rolling.moment.feature

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ActivityMomentBinding
import kr.co.rolling.moment.feature.base.BaseActivity
import kr.co.rolling.moment.feature.main.MyInfoFragment
import kr.co.rolling.moment.library.data.Constants.MOMENT_PUSH_DATA_KEY
import kr.co.rolling.moment.library.data.NavigationData
import kr.co.rolling.moment.library.network.NetworkConstants.NETWORK_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.network.data.CustomError
import kr.co.rolling.moment.library.network.data.ErrorType
import kr.co.rolling.moment.library.network.data.response.TokenInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MainViewModel
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.util.PreferenceManager
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.navigate
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Timber.d("onNewIntent: data = ${intent}")

        intent?.extras?.let {
            val data = it.getParcelableCompat(MOMENT_PUSH_DATA_KEY, NavigationData::class.java) ?: return
            // 처리 완료 후 Key 제거
            intent.removeExtra(MOMENT_PUSH_DATA_KEY)
            navigate(data)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val manager = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
        try {
            manager?.let {
                val uri = (it.primaryClip?.getItemAt(0)?.text ?: "").toString().toUri()

                if (uri.isHierarchical) {
                    val momentCode = uri.getQueryParameter(NETWORK_KEY_MOMENT_CODE)
                    momentCode?.let { code ->
                        preferenceManager.setMomentCode(code)
                        val emptyClip = ClipData.newPlainText("", "")
                        it.setPrimaryClip(emptyClip)
                    }
                }
            }

        } catch (e: Exception) {
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