package kr.co.rolling.moment.library.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

/**
 * 010PAY 사용중인 Android 권한
 */
class PermissionValues @Inject constructor() {
    /** 카메라에 대한 권한 */
    private val Cameras = arrayOf(
        Manifest.permission.CAMERA
    )

    /** 저장소에 대한 권한 */
    private val Storage = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /** 알림 대한 권한 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val Notification = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS
    )

    /** 이미지 저장소 대한 권한 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val Image = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    /** 카메라 권한 */
    val cameraValues: Array<String>
        get() = Cameras

    /** 저장소 권한 */
    val storageValues: Array<String>
        get() = Storage

    /** 알림 권한 */
    val notificationValues: Array<String>
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() = Notification

    /** 이미지 권한 */
    val imageValues: Array<String>
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() = Image
}