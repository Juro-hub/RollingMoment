package kr.co.rolling.moment.feature.trace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.databinding.BottomSheetSelectFontBinding
import kr.co.rolling.moment.feature.base.BaseBottomSheetFragment
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 작성 - 폰트 선택 BottomSheet Fragment
 */
@AndroidEntryPoint
class FontBottomSheetFragment : BaseBottomSheetFragment<BottomSheetSelectFontBinding>() {
    private val args by navArgs<FontBottomSheetFragmentArgs>()

    companion object {
        const val BUNDLE_KEY_TITLE = "bundle_key_font"
        const val BUNDLE_KEY_TITLE_DATA = "bundle_key_font_data"
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetSelectFontBinding {
        return BottomSheetSelectFontBinding.inflate(inflater, container, false)
    }

    override fun init() {
        binding.ivClose.setOnSingleClickListener { dismiss() }

        val adapter = FontAdapter()
        adapter.setSelectedItem(args.fontType ?: TraceFontType.DEFAULT)
        adapter.setClickListener { font ->
            setFragmentResult(BUNDLE_KEY_TITLE, bundleOf(BUNDLE_KEY_TITLE_DATA to font))
            dismiss()
        }

        adapter.submitList(TraceFontType.entries)
        binding.rvAdapter.adapter = adapter
    }
}