package kr.co.rolling.moment.feature.moment

import android.view.View
import androidx.navigation.fragment.findNavController
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentEnrollBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 탐색을 통하여 모먼트 진입 시 노출되는 화면
 */
class MomentEnrollFragment : BaseFragment(R.layout.fragment_moment_enroll) {
    private lateinit var binding: FragmentMomentEnrollBinding
    override fun initViewBinding(view: View) {
        binding = FragmentMomentEnrollBinding.bind(view)

        binding.btnEnroll.setOnSingleClickListener {
            findNavController().navigateSafe(MomentEnrollFragmentDirections.actionMomentEnrollFragmentToMomentDetailFragment())
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }
}