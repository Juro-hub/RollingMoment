package kr.co.rolling.moment.ui.component

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/** Dialog 화면 표시 정보 */
@Parcelize
data class CommonDialogData(
    /** 제목 */
    @SerializedName("title")
    val title: String = "",

    /** 주 내용 */
    @SerializedName("contents")
    val contents: String = "",

    /** Positive 버튼 텍스트 */
    @SerializedName("positiveText")
    val positiveText: String = "",

    /** Negative 버튼 텍스트 */
    @SerializedName("negativeText")
    val negativeText: String = "",
) : Parcelable
