package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMyInfoBinding
import kr.co.rolling.moment.feature.base.BaseActivity
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.data.response.MyPageInfo
import kr.co.rolling.moment.library.network.data.response.SplashInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MainViewModel
import kr.co.rolling.moment.library.util.landingOutLink
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.component.CommonDialogData
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import kr.co.rolling.moment.ui.util.showDialog
import timber.log.Timber

/**
 * 내정보 화면
 */
@AndroidEntryPoint
class MyInfoFragment : BaseFragment(R.layout.fragment_my_info) {
    private lateinit var binding: FragmentMyInfoBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun initViewBinding(view: View) {
        binding = FragmentMyInfoBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.myPageInfo, ::handleMyPageInfo)
        viewLifecycleOwner.observeEvent(viewModel.isWithdraw, ::handleWithdraw)
        viewLifecycleOwner.observeEvent(viewModel.isLogout, ::handleLogout)

        viewModel.requestMyPageInfo()
    }

    private fun handleLogout(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleLogout: data = ${it}")
            preferenceManager.setTokenInfo(SplashInfo("", ""))

            val navController = requireActivity().findNavController(R.id.nav_host_fragment)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true) // 백스택 모두 제거
                .build()

            navController.navigate(R.id.IntroFragment, null, navOptions)
        }
    }

    private fun handleWithdraw(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleWithdraw: data = ${it}")

            preferenceManager.setTokenInfo(SplashInfo("", ""))
            requireActivity().finishAffinity()
        }
    }

    private fun handleMyPageInfo(data: SingleEvent<MyPageInfo>) {
        data.getContentIfNotHandled()?.let { data ->
            Timber.d("handleMyPageInfo: data = ${data}")

            binding.layoutService.root.setOnSingleClickListener {
                (requireActivity() as? BaseActivity)?.landingOutLink(data.serviceUrl)
            }

            binding.layoutPolicy.root.setOnSingleClickListener {
                (requireActivity() as? BaseActivity)?.landingOutLink(data.policyUrl)
            }

            binding.tvEmail.text = data.userId
            binding.tvNickName.text = data.nickName
            binding.layoutAlarm.cbAlarm.isChecked = data.isAlarmOn
        }
    }

    private fun initUI() {
        binding.layoutAlarm.apply {
            cbAlarm.show()
            ivRight.hide()
            tvContent.text = getString(R.string.my_info_alarm)
            root.setOnSingleClickListener {
                viewModel.requestPushStatusChange()
                cbAlarm.isChecked = !cbAlarm.isChecked
            }
            cbAlarm.setOnSingleClickListener {
                viewModel.requestPushStatusChange()
            }
        }

        binding.layoutService.tvContent.text = getString(R.string.my_info_service)
        binding.layoutPolicy.tvContent.text = getString(R.string.my_info_policy)
        binding.layoutLogOut.tvContent.text = getString(R.string.my_info_logout)
        binding.layoutWithdraw.tvContent.text = getString(R.string.my_info_withdraw)
        binding.layoutWithdraw.viewDivider.hide()

        binding.layoutLogOut.root.setOnSingleClickListener {
            showDialog(CommonDialogData(title = getString(R.string.my_info_logout_dialog_title), positiveText = getString(R.string.my_info_logout_dialog_positive), negativeText = getString(R.string.no)), positiveCallback = {
                viewModel.requestLogout()
            })
        }

        binding.layoutWithdraw.root.setOnSingleClickListener {
            showDialog(CommonDialogData(title = getString(R.string.my_info_withdraw_dialog_title), positiveText = getString(R.string.my_info_withdraw_dialog_positive), negativeText = getString(R.string.no)), positiveCallback = {
                viewModel.requestWithDraw()
            })
        }
    }
}