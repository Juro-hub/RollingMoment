package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentTraceCreateBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.feature.moment.CouponImagePopUpDialog
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.library.network.data.request.RequestTrace
import kr.co.rolling.moment.library.network.data.response.CreateTraceInfo
import kr.co.rolling.moment.library.network.data.response.TraceAiInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.library.util.showToast
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import timber.log.Timber
import kotlin.random.Random


/**
 * 흔적 작성 화면
 */
@AndroidEntryPoint
class CreateTraceFragment : BaseFragment(R.layout.fragment_trace_create) {
    private lateinit var binding: FragmentTraceCreateBinding
    private val args by navArgs<CreateTraceFragmentArgs>()

    private val viewModel by activityViewModels<MomentViewModel>()

    override fun initViewBinding(view: View) {
        binding = FragmentTraceCreateBinding.bind(view)
        initUI()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.traceAiData, ::handleAiTrace)
        viewLifecycleOwner.observeEvent(viewModel.traceCreateInfo, ::handleTraceCreate)
    }

    private fun handleAiTrace(event: SingleEvent<TraceAiInfo>) {
        event.getContentIfNotHandled()?.let { data ->
            Timber.d("handleAiTrace: data = ${data}")

            binding.etTrace.setText(data.content)
        }
    }

    private fun handleTraceCreate(event: SingleEvent<CreateTraceInfo>) {
        event.getContentIfNotHandled()?.let { data ->
            Timber.d("handleTraceCreate: data = ${data}")
            showToast(getString(R.string.trace_create_done))

            val random = Random.nextInt(10) == 0
            if (data.momentCode == "MO20251071744885997781" && random) {
                CouponImagePopUpDialog().show(parentFragmentManager, "couponImagePopUp")
            }
            finishFragment()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        var font = TraceFontType.DEFAULT
        var backgroundColor = Constants.TraceBackgroundColor.NONE
        var alignment = Constants.TraceTextAlign.LEFT_ALIGN

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
            binding.groupAi.isVisible = char.isNullOrEmpty() && viewModel.traceAiData.value == null
            binding.tvLength.text = getString(R.string.edit_text_length_check, char?.length, 500)
            binding.btnConfirm.isEnabled = !char.isNullOrEmpty()
        }

        binding.btnAi.setOnSingleClickListener {
            viewModel.requestTraceAi(args.momentCode)
        }

        binding.etFont.setClickListener {
            findNavController().navigateSafe(CreateTraceFragmentDirections.actionTraceCreateFragmentToFontSelectBottomSheetFragment(TraceFontType.getFontWithTitle(binding.etFont.getData())))
        }
        binding.etFont.setText(TraceFontType.DEFAULT.title)

        setFragmentResultListener(FontBottomSheetFragment.BUNDLE_KEY_TITLE) { _, bundle ->
            font = bundle.getParcelableCompat(FontBottomSheetFragment.BUNDLE_KEY_TITLE_DATA, TraceFontType::class.java) ?: return@setFragmentResultListener
            binding.etFont.setText(font.title)
            binding.etFont.setFont(font)
            binding.etTrace.typeface = ResourcesCompat.getFont(requireContext(), font.fontRes)
        }

        binding.btnConfirm.setOnSingleClickListener {
            val data = RequestTrace(
                momentCode = args.momentCode,
                content = binding.etTrace.text.toString(),
                bgColor = backgroundColor.type,
                fontType = font.type,
                fontAlign = alignment.type
            )
            viewModel.requestTraceCreate(data)
        }

        val colorAdapter = TraceBackgroundColorAdapter()
        colorAdapter.setClickListener {
            binding.layoutTrace.setBackgroundColor(ContextCompat.getColor(requireContext(), it.color))
            backgroundColor = it
        }
        val data = Constants.TraceBackgroundColor.entries
        colorAdapter.submitList(data)
        binding.rvBackgroundColor.adapter = colorAdapter
        binding.rvBackgroundColor.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_12), horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), spanCount = 5))

        val alignmentAdapter = TraceAlignmentAdapter()
        alignmentAdapter.setClickListener { type ->
            binding.etTrace.gravity = type.gravity
            alignment = type
        }
        val alignments = Constants.TraceTextAlign.entries
        alignmentAdapter.submitList(alignments)

        binding.rvAlign.adapter = alignmentAdapter
        binding.rvAlign.addItemDecoration(CommonGridItemDecorator(verticalMargin = 0, horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_20), spanCount = 3))
    }
}