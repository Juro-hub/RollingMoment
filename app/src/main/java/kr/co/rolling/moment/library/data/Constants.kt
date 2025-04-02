package kr.co.rolling.moment.library.data

import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kr.co.rolling.moment.R

/**
 * 상수 정의
 */
object Constants {
    /** 상수 영역 */


    /** Enum 영역 */

    /** 앱 내 권한 */
    enum class PermissionType {
        /** Camera */
        CAMERA_PERMISSION,

        /** 저장소 */
        STORAGE_PERMISSION,

        /** 알림 */
        ALARM_PERMISSION
    }

    /** 흔적 작성 시 텍스트 정렬 */
    enum class TraceTextAlign(@DrawableRes val drawable: Int, @StringRes val string: Int, val gravity: Int) {
        LEFT_ALIGN(R.drawable.ic_left, R.string.trace_create_alignment_left, Gravity.START or Gravity.TOP),

        CENTER_ALIGN(R.drawable.ic_center, R.string.trace_create_alignment_center, Gravity.CENTER or Gravity.TOP),

        RIGHT_ALIGN(R.drawable.ic_right, R.string.trace_create_alignment_right, Gravity.END or Gravity.TOP)
    }

    /** 흔적 작성 시 백그라운드 색상 */
    enum class TraceBackgroundColor(@ColorRes val color: Int, val type: String) {
        NONE(R.color.CF6F6F6, "none"),

        RED(R.color.CFBD4D4, "red"),

        ORANGE(R.color.CFFEBC6, "orange"),

        YELLOW(R.color.CFEF8CC, "yellow"),

        BLUE(R.color.CDDEFFB, "blue"),

        GREEN(R.color.CD9F5ED, "green"),

        MINT(R.color.CE6F5E7, "mint"),

        PINK(R.color.CFFE4FA, "pink"),

        PURPLE(R.color.CE3E1EF, "purple"),

        NAVY(R.color.CD8E2F3, "navy")
    }
}