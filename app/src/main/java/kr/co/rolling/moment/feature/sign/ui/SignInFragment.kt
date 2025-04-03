package kr.co.rolling.moment.feature.sign.ui

import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSignInBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 이메일로 로그인 화면
 */
@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {
    private lateinit var binding: FragmentSignInBinding
    override fun initViewBinding(view: View) {
        binding = FragmentSignInBinding.bind(view)

        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.sign_in_email)
        binding.layoutToolBar.ivBack.setOnSingleClickListener {
            finishFragment()
        }

        binding.tvSignUp.setOnSingleClickListener {
            findNavController().navigateSafe(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    override fun observeViewModel() {
    }
}