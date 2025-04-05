package kr.co.rolling.moment.feature.main

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMyInfoBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show

/**
 *
 */
@AndroidEntryPoint
class MyInfoFragment : BaseFragment(R.layout.fragment_my_info) {
    private lateinit var binding: FragmentMyInfoBinding
    override fun initViewBinding(view: View) {
        binding = FragmentMyInfoBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    private fun initUI() {
        binding.layoutAlarm.apply {
            cbAlarm.show()
            ivRight.hide()
            tvContent.text = getString(R.string.my_info_alarm)
            root.setOnSingleClickListener {
                cbAlarm.isChecked = !cbAlarm.isChecked
                //TODO API 통신
            }
        }

        binding.layoutService.tvContent.text = getString(R.string.my_info_service)
        binding.layoutPolicy.tvContent.text = getString(R.string.my_info_policy)
        binding.layoutLogOut.tvContent.text = getString(R.string.my_info_logout)
        binding.layoutWithdraw.tvContent.text = getString(R.string.my_info_withdraw)
        binding.layoutWithdraw.viewDivider.hide()
    }
}