package kr.co.rolling.moment.library.data

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R

/**
 *
 */
/** 모먼트 완료 페이지 진입 유형 **/
@Parcelize
enum class MomentResultType(@StringRes val stringId: Int) : Parcelable {
    /** 생성 후 진입 */
    CREATE(R.string.moment_result_title_invite),

    /** 마감 진입 */
    DONE(R.string.moment_result_title),

    /** 초대버튼 진입 */
    INVITE(R.string.moment_result_title_share)
}