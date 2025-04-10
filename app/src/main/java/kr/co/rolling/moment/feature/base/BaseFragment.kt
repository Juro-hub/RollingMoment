package kr.co.rolling.moment.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import kr.co.rolling.moment.library.util.AndroidInfo
import kr.co.rolling.moment.library.util.EncryptManager
import kr.co.rolling.moment.library.util.PreferenceManager
import javax.inject.Inject

/**
 *
 */
abstract class BaseFragment(
    @LayoutRes val layoutId: Int
) : BaseAbstractFragment() {
    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var encryptManager: EncryptManager

    @Inject
    lateinit var androidInfo: AndroidInfo

    /**
     * 화면 Init
     * @param view 현재 노출되는 View
     */
    override fun initViewBinding(view: View) {}

    /**
     * ViewModel Observe 수행
     */
    override fun observeViewModel() {}
    override fun handleBackPressed() {
        finishFragment()
    }

    /**
     * 기본 뒤로가기 액션 처리 Skip 여부 (기본값 : false)
     */
    override fun ignoreBackPressedCallback(): Boolean {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)
        observeViewModel()

        if (!ignoreBackPressedCallback()) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        onBackPressedCallback.remove()
    }

    /**
     * Fragment 종료
     *
     * Activity Navigate에 Fragment 히스토리가
     * 쌓여 있는 경우 이전 Fragment화면으로 이동
     * 없을 경우 Activity 종료
     */
    protected fun finishFragment() {
        try {
            if (!findNavController().popBackStack()) {
                requireActivity().finish()
            }
        } catch (e: IllegalStateException) {
            requireActivity().finish()
        }
    }

}