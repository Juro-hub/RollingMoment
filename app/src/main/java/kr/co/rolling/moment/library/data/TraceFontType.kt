package kr.co.rolling.moment.library.data

import android.os.Parcelable
import androidx.annotation.FontRes
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R

/**
 *
 */
//TODO 변경 필요
@Parcelize
enum class TraceFontType(@FontRes val fontRes: Int, val title: String) : Parcelable {
    DEFAULT(R.font.pretendard_regular, "기본 글씨체"),
    LEAF(R.font.leaf, "온글잎 박다현체"),
    S_CORE(R.font.score, "에스코어 드림"),
    NAVER(R.font.naver, "네이버 마루부리");

    companion object {
        fun getFont(title: String): TraceFontType {
            return entries.find {
                it.title == title
            } ?: DEFAULT
        }
    }
}