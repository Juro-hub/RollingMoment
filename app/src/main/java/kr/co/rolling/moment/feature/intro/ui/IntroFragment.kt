package kr.co.rolling.moment.feature.intro.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.network.data.request.RequestSplash
import kr.co.rolling.moment.library.network.data.response.SplashInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.permission.PermissionManager
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.showPermissionDialog
import javax.inject.Inject

/**
 * Intro 화면
 */
@AndroidEntryPoint
class IntroFragment : BaseFragment(R.layout.fragment_intro) {
    private val viewModel: SignViewModel by activityViewModels()

    @Inject
    lateinit var permissionManager: PermissionManager

    /** 알림 권한 Launcher */
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val isGrant = permissionManager.isGrant(result)

            if (isGrant) {
                requestSplash()
            } else {
                showPermissionDialog(Constants.PermissionType.ALARM_PERMISSION) {
                    requestSplash()
                }
            }
        }

    override fun initViewBinding(view: View) {
        val rejectedPermissionList =
            permissionManager.getPostNotificationValuesPermissionDeniedList()

        if (rejectedPermissionList.isNullOrEmpty() || preferenceManager.isAlreadyShowAlarmPermission()) {
            requestSplash()
        } else {
            preferenceManager.setAlarmPermission()
            permissionLauncher.launch(rejectedPermissionList.toTypedArray())
        }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.splashInfo, ::handleSplash)
    }

    override fun handleBackPressed() {
        requireActivity().finishAffinity()
    }

    private fun handleSplash(event: SingleEvent<SplashInfo>) {
        event.getContentIfNotHandled()?.let { data ->
            // 강제 업데이트 팝업 노출
            if (data.isUpdate) {
                val bundle = Bundle()
                bundle.putParcelable(UpdateDialog.ARG_DIALOG_DATA, data)

                val updateDialog = UpdateDialog()

                updateDialog.arguments = bundle
                updateDialog.show(childFragmentManager, "updateAlert")
                return
            }
            // 자동 로그인 사용자
            if (data.accessToken.isNotEmpty()) {
                preferenceManager.setTokenInfo(data)
                findNavController().navigateSafe(IntroFragmentDirections.actionIntroFragmentToMainFragment())
                return
            }

            findNavController().navigateSafe(IntroFragmentDirections.actionIntroFragmentToSignFragment())
        }
    }

    private fun requestSplash() {
        val data = RequestSplash(
            pushToken = preferenceManager.getPushToken()
        )
        viewModel.requestSplash(data)
    }
}