package kr.co.rolling.moment.feature.sign.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSignBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import timber.log.Timber

/**
 * 로그인 수단 선택 화면
 */
@AndroidEntryPoint
class SignFragment : BaseFragment(R.layout.fragment_sign) {
    private lateinit var binding: FragmentSignBinding

    /**
     * SNS(Naver) Login Callback
     */
    private val naverLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            Timber.d("onSuccess:${NaverIdLoginSDK.getAccessToken()}")
        }

        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Timber.d("errorCode:$errorCode, errorDesc:$errorDescription")
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    /**
     * SNS(Kakao) Login Callback
     */
    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = kakaoLogin@{ token, error ->
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                return@kakaoLogin
            }

        } else {
            Timber.d("token : ${token?.accessToken}")
        }
    }

    override fun initViewBinding(view: View) {
        binding = FragmentSignBinding.bind(view)
        initSnsLogin()

        binding.layoutEmail.root.setOnSingleClickListener {
//            findNavController().navigateSafe(SignFragmentDirections.actionSignFragmentToSignInFragment())
            findNavController().navigateSafe(SignFragmentDirections.actionSignFragmentToMomentCreateFragment())
        }

        binding.layoutKakao.root.setOnSingleClickListener {
            kakaoLogin()
//            findNavController().navigateSafe(SignFragmentDirections.actionSignFragmentToTraceCreateFragment())
        }

        binding.layoutNaver.root.setOnSingleClickListener {
//            NaverIdLoginSDK.authenticate(requireContext(), naverLoginCallback)
            findNavController().navigateSafe(SignFragmentDirections.actionSignFragmentToMomentEnrollFragment())
        }

        binding.tvSIgnUp.setOnSingleClickListener {
            findNavController().navigateSafe(SignFragmentDirections.actionSignFragmentToSignUpFragment())
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    private fun initSnsLogin() {
        binding.layoutKakao.apply {
            ivSignMethod.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.kakao))
            tvSignMethod.text = getString(R.string.sign_in_kakao)
            TextViewCompat.setTextAppearance(tvSignMethod, R.style.B116Medium)
            tvSignMethod.setTextColor(ContextCompat.getColor(requireContext(), R.color.C171719))
            root.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_round_28_fae100)
        }

        binding.layoutNaver.apply {
            ivSignMethod.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_naver))
            tvSignMethod.text = getString(R.string.sign_in_naver)
            TextViewCompat.setTextAppearance(tvSignMethod, R.style.B116Medium)
            tvSignMethod.setTextColor(ContextCompat.getColor(requireContext(), R.color.CFFFFFF))
            root.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_round_28_03cf5d)
        }

        binding.layoutEmail.apply {
            ivSignMethod.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.email))
            tvSignMethod.text = getString(R.string.sign_in_email)
            TextViewCompat.setTextAppearance(tvSignMethod, R.style.B116Medium)
            tvSignMethod.setTextColor(ContextCompat.getColor(requireContext(), R.color.C171719))
            root.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_round_28_000000)
        }
    }

    private fun kakaoLogin() {
        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = kakaoLoginCallback)
        return
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = kakaoLoginCallback)
        } else {
        }

    }
}