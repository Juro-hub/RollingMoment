package kr.co.rolling.moment.feature.moment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.BottomSheetMomentCategoryBinding
import kr.co.rolling.moment.feature.base.BaseBottomSheetFragment
import kr.co.rolling.moment.library.data.MomentCategoryType
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 모먼트 카테고리 설정 BototmSheetFragment
 */
class MomentCategoryBottomSheetFragment : BaseBottomSheetFragment<BottomSheetMomentCategoryBinding>() {
    private val args by navArgs<MomentCategoryBottomSheetFragmentArgs>()

    companion object {
        const val BUNDLE_KEY_TITLE = "bundle_key_title"
        const val BUNDLE_KEY_TITLE_DATA = "bundle_key_title_data"
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetMomentCategoryBinding {
        return BottomSheetMomentCategoryBinding.inflate(inflater, container, false)
    }

    override fun init() {
        binding.ivCancel.setOnSingleClickListener { dismiss() }

        val adapter = MomentCategoryAdapter()
        adapter.setSelectedItem(args.selectedItem)
        adapter.setClickListener { title ->
            setFragmentResult(BUNDLE_KEY_TITLE, bundleOf(BUNDLE_KEY_TITLE_DATA to title))
            dismiss()
        }

        val gridLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        val data = MomentCategoryType.entries
        adapter.submitList(data)
        binding.rvAdapter.layoutManager = gridLayoutManager
        binding.rvAdapter.adapter = adapter
        binding.rvAdapter.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_8), horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_8), spanCount = 3))
    }
}