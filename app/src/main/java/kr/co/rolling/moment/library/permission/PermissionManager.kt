package kr.co.rolling.moment.library.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kr.co.rolling.moment.library.util.AndroidInfo
import javax.inject.Inject

/**
 * Android 권한 관련 처리 Class
 */
class PermissionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val androidInfo: AndroidInfo,
    private val permissionValues: PermissionValues
) {
    /**
     * 권한 상태
     */
    enum class PermissionStatus {
        /** 권한 승인 */
        GRANT,

        /** 권한 거부 */
        DENY,

        /** 권한 설정 창 이동 */
        GO_SETTING
    }

    /**
     * @return 카메라 권한에 대하여 획득해야할 경우 List Return
     */
    fun getCameraPermissionDeniedList(): List<String> {
        return getPermissionDeniedList(permissionValues.cameraValues)
    }

    /**
     * @return 저장소 권한에 대하여 획득해야할 경우 List Return
     */
    fun getStoragePermissionDeniedList(): List<String> {
        return getPermissionDeniedList(permissionValues.storageValues)
    }

    /**
     * @return 알림 권한에 대하여 획득해야할 경우 List Return
     */
    fun getPostNotificationValuesPermissionDeniedList(): List<String>? {
        return if (androidInfo.hasTiramisu()) {
            getPermissionDeniedList(permissionValues.notificationValues)
        } else {
            null
        }
    }

    /**
     * @return 이미지 획득 권한에 대하여 획득해야할 경우 List Return
     */
    fun getImageValuesPermissionDeniedList(): List<String>? {
        return if (androidInfo.hasTiramisu()) {
            getPermissionDeniedList(permissionValues.imageValues)
        } else {
            null
        }
    }

    /**
     * 거부된 권한 List 를 반환한다.
     *
     * @param values 승인 여/부 확인할 권한 목록
     * @return 거부된 권한 목록
     */
    private fun getPermissionDeniedList(values: Array<String>): List<String> {
        return values.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 모든 권한이 동의 되었는지 확인
     *
     * @param result 권한 Map
     * @return 모든 권한에 대해서 동의되었는지 여/부
     */
    fun isGrant(result: Map<String, Boolean>): Boolean {
        return !result.containsValue(false)
    }
}