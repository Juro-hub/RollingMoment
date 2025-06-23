package kr.co.rolling.moment.library.data

import android.os.Parcelable
import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R

/**
 * 상수 정의
 */
object Constants {
    /** 상수 영역 */
    const val NAVIGATION_KEY_MOMENT_CODE = "momentCode"
    const val NAVIGATION_KEY_IS_EXPIRED = "isExpired"
    const val NAVIGATION_KEY_IS_DETAIL = "isDetail"
    const val NAVIGATION_KEY_IS_OWNER = "isOwner"

    const val MOMENT_EDIT_SUCCESS = "0000"
    const val MOMENT_SHARE_KAKAO_WEB_KEY = "REGI_WEB_DOMAIN"

    const val SCHEME_SMS_TO = "smsto:"
    const val SCHEME_SMS_TO_BODY = "sms_body"
    const val SCHEME_SHARE_TYPE = "text/plain"

    const val MOMENT_SERVICE_POLICY = "https://elegant-frog-5b2.notion.site/Terms-of-Use-1bb6137f891380f2abf2ded3339a4b7f?pvs=4"
    const val MOMENT_PRIVATE_POLICY = "https://elegant-frog-5b2.notion.site/Privacy-Policy-1bb6137f891380bc8587c034d1fa0ae7?pvs=4"

    const val MOMENT_PUSH_DATA_KEY = "MOMENT_PUSH_DATA_KEY"

    const val MOMENT_PUSH_KEY_TITLE = "title"
    const val MOMENT_PUSH_KEY_BODY= "body"
    const val MOMENT_PUSH_KEY_PAYLOAD= "payload"

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
    enum class TraceTextAlign(@DrawableRes val drawable: Int, @StringRes val string: Int, val gravity: Int, val type: String) {
        LEFT_ALIGN(R.drawable.ic_left, R.string.trace_create_alignment_left, Gravity.START or Gravity.TOP, "left"),

        CENTER_ALIGN(R.drawable.ic_center, R.string.trace_create_alignment_center, Gravity.CENTER or Gravity.TOP, "center"),

        RIGHT_ALIGN(R.drawable.ic_right, R.string.trace_create_alignment_right, Gravity.END or Gravity.TOP, "right");

        companion object {
            fun getAlign(type: String): TraceTextAlign {
                return entries.find {
                    it.type == type
                } ?: LEFT_ALIGN
            }
        }
    }

    /** 흔적 작성 시 백그라운드 색상 */
    enum class TraceBackgroundColor(@ColorRes val color: Int, val type: String) {
        NONE(R.color.CF6F6F6, "gray"),

        RED(R.color.CFBD4D4, "red"),

        ORANGE(R.color.CFFEBC6, "orange"),

        YELLOW(R.color.CFEF8CC, "yellow"),

        BLUE(R.color.CDDEFFB, "blue"),

        GREEN(R.color.CD9F5ED, "green"),

        MINT(R.color.CE6F5E7, "mint"),

        PINK(R.color.CFFE4FA, "pink"),

        PURPLE(R.color.CE3E1EF, "purple"),

        NAVY(R.color.CD8E2F3, "navy");

        companion object {
            fun getColor(type: String): TraceBackgroundColor {
                return entries.find {
                    it.type == type
                } ?: NONE
            }
        }
    }

    /** 흔적 작성 시 백그라운드 색상 */
    enum class TraceTextColor(@ColorRes val color: Int, val type: String) {
        BLACK(R.color.C333333, "black"),

        GRAY(R.color.C7F7F7F, "gray"),

        BLUE(R.color.C0045DA, "blue"),

        NAVY(R.color.C00205C, "navy"),

        GREEN(R.color.C006347, "green"),

        LAVENDER(R.color.C874FFF, "lavender"),

        PURPLE(R.color.C4D1DB5, "purple"),

        PINK(R.color.CE600BB, "pink"),

        ORANGE(R.color.CF75408, "orange"),

        BROWN(R.color.C684400, "brown");

        companion object {
            fun getColor(type: String): TraceTextColor {
                return entries.find {
                    it.type == type
                } ?: BLACK
            }
        }
    }

    /** 모먼트 생성 완료 / 마감 후 결과 공유 */
    enum class MomentShareType(@DrawableRes val drawableRes: Int, @StringRes val stringRes: Int) {
        KAKAO(R.drawable.ic_kakao, R.string.moment_result_kakao),

        MESSAGE(R.drawable.ic_message, R.string.moment_result_message),

        SHARE(R.drawable.ic_shared, R.string.moment_result_share),

        COPY(R.drawable.ic_copy, R.string.moment_result_copy)
    }

    @Parcelize
    enum class MomentCategory(val code: String, @StringRes val textId: Int) : Parcelable {
        ALL("all",R.string.moment_category_all),
        WEDDING("we", R.string.moment_category_we),
        BIRTHDAY("bi", R.string.moment_category_bi),
        FIRST_BIRTHDAY("fi", R.string.moment_category_fi),
        GRADUATION("gr", R.string.moment_category_gr),
        FAREWELL("fa", R.string.moment_category_fa),
        CHRISTMAS("ch", R.string.moment_category_ch),
        TEACHERS_DAY("te", R.string.moment_category_te),
        APPRECIATION("ap", R.string.moment_category_ap),
        CHEERING("che", R.string.moment_category_che),
        CONFESSION("co", R.string.moment_category_co),
        PRAISE("pr", R.string.moment_category_pr),
        CELEBRATION("ce", R.string.moment_category_ce);

        companion object {
            fun getCategory(code: String): MomentCategory? {
                return entries.find {
                    it.code == code
                }
            }
        }
    }
}