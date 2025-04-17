package kr.co.rolling.moment.feature.moment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Base64
import android.view.View
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentCreateBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants.MOMENT_EDIT_SUCCESS
import kr.co.rolling.moment.library.data.MomentResultType
import kr.co.rolling.moment.library.network.NetworkConstants
import kr.co.rolling.moment.library.network.data.request.RequestMomentCode
import kr.co.rolling.moment.library.network.data.request.RequestMomentCreate
import kr.co.rolling.moment.library.network.data.request.RequestMomentEdit
import kr.co.rolling.moment.library.network.data.response.MomentCreateInfo
import kr.co.rolling.moment.library.network.data.response.MomentCreateResultInfo
import kr.co.rolling.moment.library.network.data.response.MomentEditInfo
import kr.co.rolling.moment.library.network.data.response.MomentImageInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.component.CommonDialogData
import kr.co.rolling.moment.ui.util.BorderTransformation
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import kr.co.rolling.moment.ui.util.showDialog
import timber.log.Timber
import java.io.ByteArrayOutputStream

/**
 * 모먼트 만들기
 */
@AndroidEntryPoint
class MomentCreateFragment : BaseFragment(R.layout.fragment_moment_create) {
    private val viewModel by activityViewModels<MomentViewModel>()
    private val args by navArgs<MomentCreateFragmentArgs>()
    private val isEdit by lazy {
        args.momentCode.isNotEmpty()
    }

    private var category: NetworkConstants.MomentCategory? = null
    private var image: MomentImageInfo? = null

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
        viewLifecycleOwner.observeEvent(viewModel.momentCode, ::handleMomentCreate)
        viewLifecycleOwner.observeEvent(viewModel.momentCreateInfo, ::handleMomentCreateInfo)
        viewLifecycleOwner.observeEvent(viewModel.momentEditInfo, ::handleMomentEditInfo)
        viewModel.requestMomentCreateInfo()
    }

    private fun handleMomentCreate(event: SingleEvent<MomentCreateResultInfo>) {
        event.getContentIfNotHandled()?.let { data ->
            Timber.d("handleMomentCreate: data = ${data}")
            if (data.momentCode == MOMENT_EDIT_SUCCESS) {
                finishFragment()
                return
            }
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentResultFragment(momentCode = data.momentCode, enterType = MomentResultType.CREATE))
        }
    }

    private fun handleMomentCreateInfo(event: SingleEvent<MomentCreateInfo>) {
        if (args.momentCode.isNotEmpty()) {
            viewModel.requestMomentEditInfo(RequestMomentCode(args.momentCode))
        }
    }

    private fun handleMomentEditInfo(event: SingleEvent<MomentEditInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentEditInfo : ${it}")
            Glide.with(requireContext())
                .load(it.coverImage?.url)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        BorderTransformation(1f, requireContext().getColor(R.color.CE0E0E2), resources.getDimensionPixelSize(R.dimen.spacing_16).toFloat()),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.spacing_16))
                    )
                )
                .into(binding.ivCover)
            binding.groupImage.show()
            binding.layoutSelectImage.hide()

            binding.etTitle.setText(it.title)
            binding.etMemo.setText(it.comment)
            it.category?.let { category ->
                binding.etCategory.setText(getString(category.textId))
            }
            binding.cbPrivate.isChecked = !it.isPublic
            category = it.category
            image = it.coverImage
            binding.btnCreate.text = getString(R.string.moment_edit_confirm)

            for (view in binding.rgDeadline.children) {
                if (view is RadioButton && view.text == getString(it.expireType.textId)) {
                    view.isChecked = true
                    break
                }
            }
        }

    }

    private fun initToolbar() {
        binding.layoutToolBar.tvToolbarTitle.text = if (isEdit) {
            getString(R.string.moment_edit_toolbar)
        } else {
            getString(R.string.moment_create_toolbar)
        }
        binding.layoutToolBar.ivBack.setOnSingleClickListener { handleBackPressed() }
    }

    private fun initUI() {
        var coverImageUri: Uri? = null

        binding.etTitle.setTextChangeListener {
            isValidButton()
        }

        binding.rgDeadline.setOnCheckedChangeListener { _, _ -> isValidButton() }

        // Image 선택 (앨범 or 카메라)
        setFragmentResultListener(MomentCoverBottomSheetFragment.BUNDLE_KEY_URI) { _, bundle ->
            coverImageUri = bundle.getParcelableCompat(MomentCoverBottomSheetFragment.BUNDLE_KEY_URI_DATA, Uri::class.java) ?: return@setFragmentResultListener
            Glide.with(requireContext())
                .load(coverImageUri)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        BorderTransformation(1f, requireContext().getColor(R.color.CE0E0E2), resources.getDimensionPixelSize(R.dimen.spacing_16).toFloat()),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.spacing_16))
                    )
                )
                .into(binding.ivCover)

            image = null

            binding.groupImage.show()
            binding.layoutSelectImage.hide()
        }

        // Image 선택 (List 중)
        setFragmentResultListener(MomentCoverBottomSheetFragment.BUNDLE_KEY_IMAGE) { _, bundle ->
            val cover = bundle.getParcelableCompat(MomentCoverBottomSheetFragment.BUNDLE_KEY_IMAGE_DATA, MomentImageInfo::class.java) ?: return@setFragmentResultListener
            Glide.with(requireContext())
                .load(cover.url)
                .fitCenter()
                .into(binding.ivCover)

            image = MomentImageInfo(
                code = cover.code,
                url = ""
            )

            binding.groupImage.show()
            binding.layoutSelectImage.hide()
        }

        binding.layoutSelectImage.setOnSingleClickListener {
            val list = viewModel.getCoverImageInfo()?.toTypedArray() ?: return@setOnSingleClickListener
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentCoverBottomSheet(list))
        }
        binding.ivCover.setOnSingleClickListener {
            val list = viewModel.getCoverImageInfo()?.toTypedArray() ?: return@setOnSingleClickListener
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentCoverBottomSheet(list))
        }

        setFragmentResultListener(MomentCategoryBottomSheetFragment.BUNDLE_KEY_TITLE) { _, bundle ->
            category = bundle.getParcelableCompat(MomentCategoryBottomSheetFragment.BUNDLE_KEY_TITLE_DATA, NetworkConstants.MomentCategory::class.java) ?: return@setFragmentResultListener
            binding.etCategory.setText(getString(category?.textId ?: -1))
        }

        binding.etCategory.setClickListener {
            val list = viewModel.getCategoryInfo()?.toTypedArray() ?: return@setClickListener
            findNavController().navigateSafe(MomentCreateFragmentDirections.actionMomentCreateFragmentToMomentCategoryBottomSheet(selectedItem = binding.etCategory.getData(), categoryList = list))
        }

        binding.btnCreate.setOnSingleClickListener {
            val expireDate = binding.root.findViewById<RadioButton>(binding.rgDeadline.checkedRadioButtonId).text.toString()

            if (args.momentCode.isNotEmpty()) {
                val coverImage = if (image == null) {
                    MomentImageInfo(
                        url = getCoverImage(coverImageUri),
                        code = ""
                    )
                } else {
                    image
                }
                val data = RequestMomentEdit(
                    title = binding.etTitle.getData(),
                    expireType = NetworkConstants.MomentExpireType.getExpireDate(requireContext(), expireDate),
                    category = category?.code ?: "",
                    comment = binding.etMemo.text.toString(),
                    isPublic = !binding.cbPrivate.isChecked,
                    coverImgId = coverImage?.code ?: "",
                    coverImgFileKey = coverImage?.url ?: "",
                    momentCode = args.momentCode
                )
                viewModel.requestMomentEdit(data)
            } else {
                val data = RequestMomentCreate(
                    title = binding.etTitle.getData(),
                    expireType = NetworkConstants.MomentExpireType.getExpireDate(requireContext(), expireDate),
                    category = category?.code ?: "",
                    comment = binding.etMemo.text.toString(),
                    isPublic = !binding.cbPrivate.isChecked,
                    coverImgId = image?.code ?: "",
                    coverImgFileKey = getCoverImage(coverImageUri)
                )
                viewModel.requestMomentCreate(data)
            }
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

    private fun getCoverImage(coverImageUri: Uri?): String {
        if (coverImageUri == null) return ""

        // Drawable → Bitmap 변환
        val bitmap = (binding.ivCover.drawable as? BitmapDrawable)?.bitmap ?: return ""

        // Bitmap → ByteArray
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        // ByteArray → Base64
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun isValidButton() {
        binding.btnCreate.isEnabled = binding.etTitle.getData().isNotEmpty() && binding.root.findViewById<RadioButton>(binding.rgDeadline.checkedRadioButtonId) != null
    }
}