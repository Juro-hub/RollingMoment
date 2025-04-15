package kr.co.rolling.moment.feature.sign.ui

import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSignUpBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.NetworkConstants
import kr.co.rolling.moment.library.network.data.request.RequestSignUp
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.component.CommonEditText
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.smoothScrollToView


/**
 * 회원가입 화면
 */
@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignViewModel by activityViewModels()

    override fun initViewBinding(view: View) {
        binding = FragmentSignUpBinding.bind(view)

        initToolBar()
        initUi()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.isSIgnUp, ::handleSignUp)
    }

    private fun handleSignUp(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            finishFragment()
        }
    }

    private fun initToolBar() {
        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.sign_up)
        binding.layoutToolBar.ivBack.setOnSingleClickListener { finishFragment() }
    }

    private fun initUi() {
        binding.layoutInput.setOnSingleClickListener {
            for (view in binding.layoutInput.children) {
                if (view.hasFocus()) {
                    view.clearFocus()
                }
            }
        }

        binding.btnSignUp.setOnSingleClickListener {
            val selectGender = binding.root.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId).text.toString()
            val data = RequestSignUp(
                userId = binding.etEmail.getData(),
                password = encryptManager.setCBCMsg(binding.etPassword.getData()),
                passwordConfirm = encryptManager.setCBCMsg(binding.etPasswordConfirm.getData()),
                gender = NetworkConstants.GenderType.getGender(requireContext(), selectGender).type,
                nickName = binding.etNickName.getData()
            )
            encryptManager.decrypt(data.password)
            viewModel.requestSignUp(data)
        }

        binding.etEmail.setTextChangeListener {
            val isEmailRegex = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            binding.etEmail.setError(!isEmailRegex)
        }

        binding.etPassword.setTextChangeListener {
            val isPasswordRegex = isPasswordRegex(password = it)
            binding.etPassword.setError(!isPasswordRegex)
            checkPasswordConfirm()
        }

        binding.etPasswordConfirm.setTextChangeListener {
            checkPasswordConfirm()
        }

        val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.scrollView.smoothScrollToView(view, -30, 100L)
                }, 100)
            } else {
                updateButtonStatus()
            }
        }

        for (v in binding.layoutInput.children) {
            if (v is CommonEditText) {
                v.setFocusListener(focusChangeListener)
            }
        }
    }

    /**
     * 비밀번호 정규식 확인
     *
     * @param password 사용자 입력 비밀번호
     * @return 정규식 여부
     */
    private fun isPasswordRegex(password: String): Boolean {
        // 길이 체크
        if (password.length !in 8..16) return false

        // 각 문자 타입 체크
        val hasLower = password.any { it.isLowerCase() }
        val hasUpper = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        // 조건 만족한 종류 개수 (3개 이상)
        val count = listOf(hasLower, hasUpper, hasDigit, hasSpecial).count { it }
        return count >= 3
    }

    /**
     * 비밀번호와 비밀번호확인 동일 여/부
     */
    private fun checkPasswordConfirm() {
        if (binding.etPasswordConfirm.getData().isEmpty()) {
            return
        }

        val isError = binding.etPasswordConfirm.getData() != binding.etPassword.getData()
        binding.etPasswordConfirm.setError(isError)

        if (!isError) {
            updateButtonStatus()
        }
    }

    /**
     * 필수 데이터 입력한 경우 버튼 활성화
     */
    private fun updateButtonStatus() {
        var isValid = true
        for (view in binding.layoutInput.children) {
            if (view is CommonEditText && view.isValid()) {
                continue
            } else {
                if (isRegexView(view)) {
                    isValid = false
                }
            }
        }
        binding.btnSignUp.isEnabled = isValid && binding.radioGroup.checkedRadioButtonId != -1
    }

    private fun isRegexView(view: View): Boolean {
        return view != binding.etNickName && view is CommonEditText
    }
}