package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentTraceCreateBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.util.setOnSingleClickListener


/**
 *
 */
class CreateTraceFragment : BaseFragment(R.layout.fragment_trace_create) {
    private lateinit var binding: FragmentTraceCreateBinding

    // TODO ViewModel Data 연동
    private var isCheckAi = false

    override fun initViewBinding(view: View) {
        binding = FragmentTraceCreateBinding.bind(view)
        initUI()
        initAdapter()
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        binding.layoutToolBar.ivBack.setOnSingleClickListener { finishFragment() }
        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.trace_create_toolbar)

        binding.tvLength.text = getString(R.string.edit_text_length_check, 0, 500)

        // ScrollView 와 EditText Scroll 겹치는 이슈 수정
        binding.etTrace.setOnTouchListener { v, event ->
            if (v.id == binding.etTrace.id) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        binding.etTrace.addTextChangedListener { char ->
            binding.groupAi.isVisible = char.isNullOrEmpty() && !isCheckAi
            binding.tvLength.text = getString(R.string.edit_text_length_check, char?.length, 500)
        }

        // TODO API 연동
        binding.btnAi.setOnSingleClickListener {
            isCheckAi = true
        }

        binding.etFont.setClickListener {
            findNavController().navigateSafe(CreateTraceFragmentDirections.actionTraceCreateFragmentToFontSelectBottomSheetFragment(TraceFontType.getFont(binding.etFont.getData())))
        }
        binding.etFont.setText(TraceFontType.DEFAULT.title)

        setFragmentResultListener(FontBottomSheetFragment.BUNDLE_KEY_TITLE) { _, bundle ->
            val font = bundle.getParcelableCompat(FontBottomSheetFragment.BUNDLE_KEY_TITLE_DATA, TraceFontType::class.java) ?: return@setFragmentResultListener
            binding.etFont.setText(font.title)
            binding.etFont.setFont(font)
            binding.etTrace.typeface = ResourcesCompat.getFont(requireContext(), font.fontRes)
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun initAdapter() {
        val colorAdapter = TraceBackgroundColorAdapter()
        colorAdapter.setClickListener {
            binding.layoutTrace.setBackgroundColor(ContextCompat.getColor(requireContext(), it.color))
        }
        val data = Constants.TraceBackgroundColor.entries
        colorAdapter.submitList(data)
        binding.rvBackgroundColor.adapter = colorAdapter
        binding.rvBackgroundColor.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_12), horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), spanCount = 5))

        val alignmentAdapter = TraceAlignmentAdapter()
        alignmentAdapter.setClickListener { type ->
            binding.etTrace.gravity = type.gravity
        }
        val alignments = Constants.TraceTextAlign.entries
        alignmentAdapter.submitList(alignments)

        binding.rvAlign.adapter = alignmentAdapter
        binding.rvAlign.addItemDecoration(CommonGridItemDecorator(verticalMargin = 0, horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_20), spanCount = 3))
    }
}