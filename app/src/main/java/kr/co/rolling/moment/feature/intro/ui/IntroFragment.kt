package kr.co.rolling.moment.feature.intro.ui

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.co.rolling.moment.R
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.viewmodel.SignViewModel
import kr.co.rolling.moment.library.util.navigateSafe
import timber.log.Timber

/**
 * Intro 화면
 */
@AndroidEntryPoint
class IntroFragment : BaseFragment(R.layout.fragment_intro) {
    private val viewModel: SignViewModel by activityViewModels()

    override fun initViewBinding(view: View) {
        Timber.d("MINSEOK ${KakaoSdk.keyHash}")
        //TODO Intro API 연동 필요
        CoroutineScope(Dispatchers.IO).launch {
            delay(500L)
            CoroutineScope(Dispatchers.Main).launch {
                findNavController().navigateSafe(IntroFragmentDirections.actionIntroFragmentToSignInFragment())
            }
        }
    }

    override fun observeViewModel() {
//        viewLifecycleOwner.observeEvent()
    }
}