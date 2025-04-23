package kr.co.rolling.moment.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.databinding.BottomSheetMomentEditBinding
import kr.co.rolling.moment.feature.base.BaseBottomSheetFragment
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 모먼트 수정 관련 바텀 시트
 */
@AndroidEntryPoint
class MomentEditBottomSheetFragment : BaseBottomSheetFragment<BottomSheetMomentEditBinding>() {
    private val args by navArgs<MomentEditBottomSheetFragmentArgs>()

    companion object {
        const val BUNDLE_KEY_EDIT = "bundle_key_edit"
        const val BUNDLE_KEY_EDIT_CODE = "bundle_key_edit_code"
        const val BUNDLE_KEY_EDIT_TYPE = "isDelete"
        const val BUNDLE_KEY_SAVE = "isSave"
        const val BUNDLE_KEY_REPORT ="isReport"
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetMomentEditBinding {
        return BottomSheetMomentEditBinding.inflate(layoutInflater, container, false)
    }

    override fun init() {
        binding.tvEdit.setOnSingleClickListener {
            setFragmentResult(BUNDLE_KEY_EDIT, bundleOf(BUNDLE_KEY_EDIT_CODE to args.momentCode, BUNDLE_KEY_EDIT_TYPE to true))
            dismiss()
        }

        binding.tvDelete.setOnSingleClickListener {
            setFragmentResult(BUNDLE_KEY_EDIT, bundleOf(BUNDLE_KEY_EDIT_CODE to args.momentCode))
            dismiss()
        }

        binding.tvSave.setOnSingleClickListener {
            setFragmentResult(BUNDLE_KEY_EDIT, bundleOf(BUNDLE_KEY_SAVE to true))
            dismiss()
        }

        binding.tvReport.setOnSingleClickListener {
            setFragmentResult(BUNDLE_KEY_EDIT, bundleOf(BUNDLE_KEY_EDIT_CODE to args.momentCode, BUNDLE_KEY_REPORT to true))
            dismiss()
        }

        binding.tvDelete.isVisible = args.isOwner
        binding.tvEdit.isVisible = !args.isExpired && args.isOwner
        binding.tvSave.isVisible = args.isDetail
        binding.tvReport.isVisible = args.isDetail
    }
}