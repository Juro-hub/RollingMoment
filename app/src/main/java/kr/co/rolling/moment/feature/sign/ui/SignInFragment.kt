package kr.co.rolling.moment.feature.sign.ui

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSignInBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.data.request.RequestLogin
import kr.co.rolling.moment.library.network.data.response.TokenInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 이메일로 로그인 화면
 */
@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignViewModel by activityViewModels()

    override fun initViewBinding(view: View) {
        binding = FragmentSignInBinding.bind(view)

        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.sign_in_email)
        binding.layoutToolBar.ivBack.setOnSingleClickListener {
            finishFragment()
        }

        binding.tvSignUp.setOnSingleClickListener {
            findNavController().navigateSafe(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.btnLogin.setOnSingleClickListener {
            viewModel.requestSignIn(
                RequestLogin(
                    userId = binding.etEmail.getData(),
                    password = encryptManager.setCBCMsg(binding.etPassword.getData())
                )
            )
        }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.tokenInfo, ::handleLogin)
    }

    private fun handleLogin(event: SingleEvent<TokenInfo>) {
        event.getContentIfNotHandled()?.let { data ->
            preferenceManager.setTokenInfo(data)

            findNavController().navigateSafe(SignInFragmentDirections.actionSignInFragmentToMainFragment())
        }
    }
}