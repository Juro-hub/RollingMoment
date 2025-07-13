package kr.co.rolling.moment.feature.main

import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentInfoEditBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.NetworkConstants
import kr.co.rolling.moment.library.network.data.request.RequestUserInfo
import kr.co.rolling.moment.library.network.data.response.UserInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MainViewModel
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.library.util.showToast
import kr.co.rolling.moment.ui.component.CommonEditText
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import timber.log.Timber

class InfoEditFragment : BaseFragment(R.layout.fragment_info_edit) {
    private lateinit var binding: FragmentInfoEditBinding

    private val viewModel by activityViewModels<MainViewModel>()

    override fun initViewBinding(view: View) {
        binding = FragmentInfoEditBinding.bind(view)

        initToolBar()
        initUI()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.userInfo, ::handleUserInfo)
        viewLifecycleOwner.observeEvent(viewModel.isUserInfoUpdate, ::handleUserInfoUpdate)

        viewModel.requestUserInfo()
    }

    private fun handleUserInfo(event: SingleEvent<UserInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleUserInfo Called : $it")

            binding.etEmail.setText(it.userId)
            binding.etNickName.setText(it.nickname)

            for (view in binding.radioGroup.children) {
                if (view is RadioButton && view.text == getString(it.gender.textId)) {
                    view.isChecked = true
                    break
                }
            }
        }
    }

    private fun handleUserInfoUpdate(event: SingleEvent<Boolean>){
        event.getContentIfNotHandled()?.let {
            if(it){
                showToast(getString(R.string.moment_user_info_update_success))
                finishFragment()
            }
        }
    }

    private fun initToolBar() {
        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.moment_user_info_edit_title)
        binding.layoutToolBar.ivBack.setOnSingleClickListener { finishFragment() }
    }

    private fun initUI() {
        binding.root.setOnSingleClickListener {
            for (view in binding.root.children) {
                if (view.hasFocus()) {
                    view.clearFocus()
                }
            }
        }

        binding.etEmail.setTextChangeListener {
            val isEmailRegex = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            binding.etEmail.setError(!isEmailRegex)
        }

        val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                updateButtonStatus()
            }
        }

        for (v in binding.root.children) {
            if (v is CommonEditText) {
                v.setFocusListener(focusChangeListener)
            }
        }

        binding.etNickName.setTextChangeListener {
            val input = it.toString()

            val isValid = input.matches(Regex("^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]+$"))
            binding.etNickName.setError(!isValid)
        }

        binding.btnConfirm.setOnSingleClickListener{
            val selectGender = binding.root.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()

            viewModel.requestUserInfoUpdate(
                RequestUserInfo(
                    nickname = binding.etNickName.getData(),
                    userId =  binding.etEmail.getData(),
                    gender = NetworkConstants.GenderType.getGender(requireContext(), selectGender).type
                )
            )
        }
    }

    /**
     * 필수 데이터 입력한 경우 버튼 활성화
     */
    private fun updateButtonStatus() {
        var isValid = true
        for (view in binding.root.children) {
            if (view is CommonEditText && view.isValid()) {
                continue
            } else {
                if (isRegexView(view)) {
                    isValid = false
                }
            }
        }
        binding.btnConfirm.isEnabled = isValid && binding.radioGroup.checkedRadioButtonId != -1
    }

    private fun isRegexView(view: View): Boolean {
        return view is CommonEditText
    }
}