package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentDetailBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.feature.main.MomentEditBottomSheetFragment
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_DETAIL
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_EXPIRED
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_OWNER
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.data.MomentResultType
import kr.co.rolling.moment.library.network.data.request.RequestMomentCode
import kr.co.rolling.moment.library.network.data.response.MomentDetailInfo
import kr.co.rolling.moment.library.network.data.response.MomentTraceInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import kr.co.rolling.moment.ui.util.showExpandableText
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * 모먼트 상세 조회 화면
 */
@AndroidEntryPoint
class MomentDetailFragment : BaseFragment(R.layout.fragment_moment_detail) {
    private lateinit var binding: FragmentMomentDetailBinding
    private val args by navArgs<MomentDetailFragmentArgs>()
    private val viewModel by activityViewModels<MomentViewModel>()

    private val adapter by lazy {
        MomentDetailTraceAdapter()
    }

    override fun initViewBinding(view: View) {
        binding = FragmentMomentDetailBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.momentDetailInfo, ::handleMomentDetail)
        viewLifecycleOwner.observeEvent(viewModel.momentTraceInfo, ::handleTraceInfo)
        viewModel.requestMomentDetail(RequestMomentCode(args.momentCode))
    }

    private fun handleTraceInfo(event: SingleEvent<MomentTraceInfo>) {
        event.getContentIfNotHandled()?.let {
            val code = it.code
            val updateReaction = it.reactions

            adapter.updateReaction(code, updateReaction)
        }
    }

    private fun handleMomentDetail(event: SingleEvent<MomentDetailInfo>) {
        event.getContentIfNotHandled()?.let { data ->
            Timber.d("handleMomentDetail: data = ${data}")

            Glide.with(requireContext())
                .load(data.coverImageUrl)
                .centerCrop()
                .into(binding.ivImage)

            binding.tvDeadline.text = data.deadline
            binding.tvDeadline.isVisible = data.deadline.isNotEmpty()
            if (data.isExpired) {
                binding.tvDeadline.setBackgroundResource(R.drawable.shape_4_e7f5e7)
                binding.tvDeadline.setTextColor(requireContext().getColor(R.color.C00BF40))
                binding.btnTraceInvite.text = getString(R.string.moment_detail_expired)
            } else {
                binding.tvDeadline.setBackgroundResource(R.drawable.shape_4_eae4f8)
                binding.tvDeadline.setTextColor(requireContext().getColor(R.color.C874FFF))
                binding.btnTraceInvite.text = getString(R.string.moment_detail_invite)
            }

            data.category?.let {
                binding.tvCategory.text = getString(data.category.textId)
                binding.tvCategory.show()
            }
            binding.tvMomentTitle.text = data.title
            binding.tvContent.showExpandableText(
                data.content
            )
            binding.tvDate.text = data.period

            binding.layoutEmpty.isVisible = data.traces.isNullOrEmpty()
            binding.layoutTrace.isVisible = data.traces?.isNotEmpty() == true
            binding.tvTraceCount.text = getString(R.string.moment_detail_trace_count, data.traces?.size ?: 0)

            adapter.setClickListener {
                viewModel.requestTraceReaction(it)
            }
            adapter.submitList(data.traces)

            binding.layoutToolBar.ivClose.setOnSingleClickListener {
                val bottomSheet = MomentEditBottomSheetFragment()
                bottomSheet.arguments = bundleOf(NAVIGATION_KEY_MOMENT_CODE to args.momentCode, NAVIGATION_KEY_IS_EXPIRED to data.isExpired, NAVIGATION_KEY_IS_DETAIL to true, NAVIGATION_KEY_IS_OWNER to data.isOwner)
                bottomSheet.show(parentFragmentManager, "MomentEditBottomSheet")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        binding.rvTrace.adapter = adapter

        binding.btnPlusTrace.setOnSingleClickListener {
            findNavController().navigateSafe(MomentDetailFragmentDirections.actionMomentDetailFragmentToTraceCreateFragment(args.momentCode))
        }

        binding.btnTraceInvite.setOnSingleClickListener {
            invite()
        }

        binding.btnInvite.setOnSingleClickListener {
            invite()
        }

        binding.layoutToolBar.ivBack.setOnSingleClickListener {
            finishFragment()
        }

        binding.layoutToolBar.ivClose.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_vertical_dots))
        binding.layoutToolBar.ivClose.show()


        setFragmentResultListener(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT) { _, bundle ->
            val isSave = bundle.getBoolean(MomentEditBottomSheetFragment.BUNDLE_KEY_SAVE)
            if (isSave) {
                captureAndSaveScrollView()
            }

            val code = bundle.getString(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_CODE) ?: return@setFragmentResultListener
            val isEdit = bundle.getBoolean(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_TYPE)

            if (isEdit) {
                findNavController().navigateSafe(MomentDetailFragmentDirections.actionMomentDetailFragmentToMomentCreateFragment(code))
            } else {
                viewModel.requestMomentDelete(code)
            }
        }
    }

    private fun invite() {
        val type = if (viewModel.isMomentExpired()) {
            MomentResultType.DONE
        } else {
            MomentResultType.INVITE
        }
        findNavController().navigateSafe(MomentDetailFragmentDirections.actionMomentDetailFragmentToMomentResultFragment(momentCode = args.momentCode, enterType = type))
    }

    private fun captureAndSaveScrollView() {
        val contentView = binding.layoutScroll.getChildAt(0)

        // 1. 캡처 준비
        binding.layoutScroll.post {
            val width = contentView.width
            val height = contentView.height

            if (width == 0 || height == 0) {
                Toast.makeText(context, "화면이 아직 준비되지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@post
            }

            // 2. 비트맵 생성 및 그리기
            val bitmap = createBitmap(width, height)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE) // 배경색 흰색 지정
            contentView.draw(canvas)

            // 3. 파일 저장
            val filename = "layout_${System.currentTimeMillis()}.jpg"
            val fos: OutputStream?
            var imageUri: Uri? = null

            if (androidInfo.hasQ()) {
                val resolver = requireContext().contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
                }

                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/MyApp"
                val file = File(imagesDir)
                if (!file.exists()) file.mkdirs()
                val image = File(file, filename)
                fos = FileOutputStream(image)
                imageUri = Uri.fromFile(image)

                requireContext().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

            Toast.makeText(context, "갤러리에 저장되었습니다", Toast.LENGTH_SHORT).show()
        }
    }


}