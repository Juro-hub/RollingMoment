package kr.co.rolling.moment.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.databinding.BottomSheetMomentEditBinding
import kr.co.rolling.moment.databinding.BottomSheetTraceMoreBinding
import kr.co.rolling.moment.feature.base.BaseBottomSheetFragment
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 더보기 바텀 시트
 */
@AndroidEntryPoint
class TraceMoreBottomSheetFragment : BaseBottomSheetFragment<BottomSheetTraceMoreBinding>() {
    private val args by navArgs<TraceMoreBottomSheetFragmentArgs>()

    companion object {
        const val BUNDLE_KEY_MORE = "bundle_key_more"
        const val BUNDLE_KEY_MORE_CODE = "bundle_key_more_code"
        const val BUNDLE_KEY_REPORT ="isReport"
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetTraceMoreBinding {
        return BottomSheetTraceMoreBinding.inflate(layoutInflater, container, false)
    }

    override fun init() {
        binding.tvReport.setOnSingleClickListener {
            setFragmentResult(BUNDLE_KEY_MORE, bundleOf(BUNDLE_KEY_MORE_CODE to args.traceCode, BUNDLE_KEY_REPORT to true))
            dismiss()
        }
    }
}