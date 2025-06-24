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
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentTraceCreateBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.feature.moment.CouponImagePopUpDialog
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.library.network.data.request.RequestTrace
import kr.co.rolling.moment.library.network.data.request.RequestTraceEdit
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
    private var font = TraceFontType.DEFAULT

    private val textColorAdapter by lazy{
        TraceTextColorAdapter()
    }

    private val alignmentAdapter by lazy{
        TraceAlignmentAdapter()
    }


    private val backgroundColorAdapter by lazy{
        TraceBackgroundColorAdapter()
    }

    override fun initViewBinding(view: View) {
        binding = FragmentTraceCreateBinding.bind(view)
        initUI()
        initSetData()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.traceAiData, ::handleAiTrace)
        viewLifecycleOwner.observeEvent(viewModel.traceCreateInfo, ::handleTraceCreate)
        viewLifecycleOwner.observeEvent(viewModel.traceEditInfo, ::handleTraceEdit)
    }

    private fun handleTraceEdit(event: SingleEvent<Boolean>){
        event.getContentIfNotHandled()?.let { data ->
            Timber.d("handleTraceEdit: data = ${data}")

            finishFragment()
        }
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

            finishFragment()
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun initUI() {
        var backgroundColor = Constants.TraceBackgroundColor.NONE
        var alignment = Constants.TraceTextAlign.LEFT_ALIGN
        var textColor = Constants.TraceTextColor.BLACK

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
            if(args.traceInfo != null){
                val data = RequestTraceEdit(
                    traceCode = args.traceInfo?.code ?: "",
                    content = binding.etTrace.text.toString(),
                    bgColor = backgroundColor.type,
                    fontType = font.type,
                    fontAlign = alignment.type,
                    isAnonymous = binding.cbAnonymous.isChecked,
                    textColor = textColor.type
                )
                viewModel.requestTraceEdit(data)
            }else{
                val data = RequestTrace(
                    momentCode = args.momentCode,
                    content = binding.etTrace.text.toString(),
                    bgColor = backgroundColor.type,
                    fontType = font.type,
                    fontAlign = alignment.type,
                    isAnonymous = binding.cbAnonymous.isChecked,
                    textColor = textColor.type
                )
                viewModel.requestTraceCreate(data)
            }
        }

        backgroundColorAdapter.setClickListener {
            if(it.color != null){
                binding.layoutTrace.setBackgroundColor(ContextCompat.getColor(requireContext(), it.color))
                backgroundColor = it
            }else{
                it.drawable?.let { drawable ->
                    binding.layoutTrace.setBackgroundResource(drawable)
                    backgroundColor = it
                }
            }

        }
        backgroundColorAdapter.submitList(Constants.TraceBackgroundColor.entries)
        binding.rvBackgroundColor.adapter = backgroundColorAdapter
        binding.rvBackgroundColor.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_12), horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), spanCount = 5))

        alignmentAdapter.setClickListener { type ->
            binding.etTrace.gravity = type.gravity
            alignment = type
        }
        val alignments = Constants.TraceTextAlign.entries
        alignmentAdapter.submitList(alignments)

        textColorAdapter.setClickListener {
            binding.etTrace.setTextColor(requireContext().getColor(it.color))
            textColor = it
        }
        textColorAdapter.submitList(Constants.TraceTextColor.entries)
        binding.rvTextColor.adapter = textColorAdapter
        binding.rvTextColor.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_12), horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), spanCount = 5))

        binding.rvAlign.adapter = alignmentAdapter
        binding.rvAlign.addItemDecoration(CommonGridItemDecorator(verticalMargin = 0, horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_20), spanCount = 3))

        val emojiAdapter = TraceEmojiAdapter()
        emojiAdapter.submitList(resources.getStringArray(R.array.trace_create_emoji_list).toMutableList())
        emojiAdapter.setClickListener {
            binding.etTrace.setText(binding.etTrace.text.toString()+it)
            binding.etTrace.setSelection(binding.etTrace.text.length)
        }
        val layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            flexWrap = FlexWrap.WRAP
        }
        binding.rvEmoji.layoutManager = layoutManager
        binding.rvEmoji.adapter = emojiAdapter
        binding.rvEmoji.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_12), horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), spanCount = 3))
    }

    private fun initSetData(){
        val data = args.traceInfo ?: return

        binding.etTrace.setText(data.content)
        alignmentAdapter.setItem(data.alignment)
        textColorAdapter.setItem(data.textColor)
        backgroundColorAdapter.setItem(data.color)

        binding.etFont.setText(data.font.title)
        binding.etFont.setFont(data.font)
        font = data.font

        binding.etTrace.typeface = ResourcesCompat.getFont(requireContext(), data.font.fontRes)
        binding.cbAnonymous.isChecked = data.nickname.contains("익명")
    }
}