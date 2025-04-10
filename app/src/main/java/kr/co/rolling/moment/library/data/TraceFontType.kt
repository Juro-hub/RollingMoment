package kr.co.rolling.moment.library.data

import android.os.Parcelable
import androidx.annotation.FontRes
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R

/**
 * 흔적 생성 시 사용하는 글꼴
 */
@Parcelize
enum class TraceFontType(@FontRes val fontRes: Int, val title: String, val type: String) : Parcelable {
    DEFAULT(R.font.pretendard_regular, "기본 글씨체", "de"),
    LEAF(R.font.leaf, "온글잎 박다현체", "on"),
    S_CORE(R.font.score, "에스코어 드림", "es"),
    NAVER(R.font.naver, "네이버 마루부리", "na");

    companion object {
        fun getFontWithTitle(title: String): TraceFontType {
            return entries.find {
                it.title == title
            } ?: DEFAULT
        }

        fun getFontWithType(type: String): TraceFontType {
            return entries.find {
                it.type == type
            } ?: DEFAULT
        }
    }
}