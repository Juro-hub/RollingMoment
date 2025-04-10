package kr.co.rolling.moment.feature.moment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.databinding.BottomSheetMomentCoverBinding
import kr.co.rolling.moment.feature.base.BaseBottomSheetFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.network.data.response.MomentImageInfo
import kr.co.rolling.moment.library.permission.PermissionManager
import kr.co.rolling.moment.library.util.AndroidInfo
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.showPermissionDialog
import java.io.File
import javax.inject.Inject

/**
 * 모먼트 커버 설정 바텀시트
 */
@AndroidEntryPoint
class MomentCoverBottomSheetFragment : BaseBottomSheetFragment<BottomSheetMomentCoverBinding>() {
    companion object {
        /** 앨범 / 카메라 이미지 선택 */
        const val BUNDLE_KEY_URI = "bundle_key_uri"
        const val BUNDLE_KEY_URI_DATA = "bundle_key_uri_data"

        /** 커버 이미지 선택 */
        const val BUNDLE_KEY_IMAGE = "bundle_key_image"
        const val BUNDLE_KEY_IMAGE_DATA = "bundle_key_image_data"
    }

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var androidInfo: AndroidInfo

    private val args by navArgs<MomentCoverBottomSheetFragmentArgs>()

    private var capturedImageUri: Uri? = null

    /** 저장소 권한 Launcher */
    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val isGrant = permissionManager.isGrant(result)

            if (isGrant) {
                getImageFromGallery()
            } else {
                showPermissionDialog(Constants.PermissionType.STORAGE_PERMISSION)
            }
        }

    private val uriCallback: ((image: MomentImageInfo) -> Unit) = { image ->
        setFragmentResult(BUNDLE_KEY_IMAGE, bundleOf(BUNDLE_KEY_IMAGE_DATA to image))
        dismiss()
    }

    /** 카메라 권한 Launcher */
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val isGrant = permissionManager.isGrant(result)

            if (isGrant) {
                takePhoto()
            } else {
                showPermissionDialog(Constants.PermissionType.CAMERA_PERMISSION)
            }
        }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = if (result.data == null) {
                // Camera 사진 촬영
                capturedImageUri
            } else {
                val intent = checkNotNull(result.data)
                intent.data
            } ?: return@registerForActivityResult

            setFragmentResult(BUNDLE_KEY_URI, bundleOf(BUNDLE_KEY_URI_DATA to imageUri))
            dismiss()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetMomentCoverBinding {
        return BottomSheetMomentCoverBinding.inflate(layoutInflater, container, false)
    }

    override fun init() {
        binding.ivCancel.setOnSingleClickListener { dismiss() }

        val adapter = MomentCreateCoverAdapter()
        adapter.setClickListener(uriCallback)
        val list = args.coverList.toMutableList()
        adapter.submitList(list)

        initUI()
    }


    private fun initUI() {
        binding.layoutCamera.setOnSingleClickListener {
            val rejectedPermissionList = permissionManager.getCameraPermissionDeniedList()

            if (rejectedPermissionList.isNotEmpty()) {
                cameraPermissionLauncher.launch(rejectedPermissionList.toTypedArray())
            } else {
                takePhoto()
            }
        }

        binding.layoutAlbum.setOnSingleClickListener {
            val rejectedPermissionList = if (androidInfo.hasTiramisu()) {
                permissionManager.getImageValuesPermissionDeniedList()
            } else {
                permissionManager.getStoragePermissionDeniedList()
            }

            if (!rejectedPermissionList.isNullOrEmpty()) {
                storagePermissionLauncher.launch(rejectedPermissionList.toTypedArray())
            } else {
                getImageFromGallery()
            }
        }
    }

    private fun getImageFromGallery() {
        val intent = Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "choose Image")
        cameraLauncher.launch(intent)
    }

    private fun takePhoto() {
        val imageFile = File.createTempFile("camera_", ".jpg", requireContext().cacheDir)
        capturedImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        cameraLauncher.launch(intent)
    }
}