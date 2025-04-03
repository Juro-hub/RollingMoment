package kr.co.rolling.moment.feature.moment

import android.net.Uri
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentCreateBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.component.CommonDialogData
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import kr.co.rolling.moment.ui.util.showDialog

/**
 * 모먼트 만들기
 */
class MomentCreateFragment : BaseFragment(R.layout.fragment_moment_create) {
    private lateinit var binding: FragmentMomentCreateBinding
    override fun handleBackPressed() {
        cancelCreateDialog {
            super.handleBackPressed()
        }
    }

    override fun initViewBinding(view: View) {
        binding = FragmentMomentCreateBinding.bind(view)

        initToolbar()
        initUI()
    }

    override fun observeViewModel() {
    }

    private fun initToolbar() {
        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.moment_create_toolbar)
        binding.layoutToolBar.ivBack.setOnSingleClickListener { handleBackPressed() }
    }

    private fun initUI() {
        setFragmentResultListener(MomentCoverBottomSheetFragment.BUNDLE_KEY_URI) { _, bundle ->
            val uri = bundle.getParcelableCompat(MomentCoverBottomSheetFragment.BUNDLE_KEY_URI_DATA, Uri::class.java) ?: return@setFragmentResultListener
            Glide.with(requireContext())
                .load(uri)
                .fitCenter()
                .into(binding.ivCover)

            binding.ivCover.show()
            binding.layoutSelectImage.hide()
        }

        binding.layoutSelectImage.setOnSingleClickListener {
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentCoverBottomSheet())
        }
        binding.ivCover.setOnSingleClickListener {
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentCoverBottomSheet())
        }

        setFragmentResultListener(MomentCategoryBottomSheetFragment.BUNDLE_KEY_TITLE) { _, bundle ->
            val title = bundle.getString(MomentCategoryBottomSheetFragment.BUNDLE_KEY_TITLE_DATA, null) ?: return@setFragmentResultListener
            binding.etCategory.setText(title)
        }
        binding.etCategory.setClickListener {
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentCategoryBottomSheet(binding.etCategory.getData()))
        }
    }

    private fun cancelCreateDialog(listener: (() -> Unit)? = null) {
        showDialog(
            CommonDialogData(
                title = getString(R.string.moment_create_cancel_title),
                contents = getString(R.string.moment_create_cancel_content),
                positiveText = getString(R.string.moment_create_cancel_positive),
                negativeText = getString(R.string.no)
            ), positiveCallback = { listener?.invoke() }
        )
    }
}