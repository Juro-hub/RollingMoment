package kr.co.rolling.moment.feature.base

import android.view.View
import androidx.fragment.app.Fragment

/**
 * Fragment 화면 추상클래스 정의
 *
 * BaseFragment를 상속 받은 클래스에서 필요에 따라 아래 함수 override 하여 처리
 */
abstract class BaseAbstractFragment : Fragment() {
    /**
     * 화면 초기화 정의
     *
     * @param view : 화면 Root View 객체
     */
    abstract fun initViewBinding(view: View)

    /**
     * ViewModel observe 설정
     */
    abstract fun observeViewModel()

    /**
     * 뒤로가기 액션 처리
     */
    abstract fun handleBackPressed()

    /**
     * 뒤로가기 액션 처리 Skip
     */
    abstract fun ignoreBackPressedCallback(): Boolean
}