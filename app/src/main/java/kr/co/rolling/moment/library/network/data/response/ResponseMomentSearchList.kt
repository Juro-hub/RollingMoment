package kr.co.rolling.moment.library.network.data.response

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.network.NetworkConstants

/**
 * 모먼토 조회
 */
@Parcelize
data class ResponseMomentSearchList(
    @SerializedName("results")
    val momentList: List<ResponseMoment>? = null,
) : Parcelable

@Parcelize
data class MomentListSearchInfo(
    val momentList: List<MomentInfo>? = null,
) : Parcelable

fun ResponseMomentSearchList.toEntity(context: Context) = MomentListSearchInfo(
    momentList = this.momentList?.map {
        MomentInfo(
            code = it.code,
            coverImgUrl = it.coverImgUrl,
            title = it.title,
            deadLine = it.deadline,
            deadLineText = if (it.deadline == -1) {
                context.getString(R.string.moment_deadline_expired)
            } else if (it.deadline == 0) {
                context.getString(R.string.moment_expired_soon)
            } else {
                context.getString(R.string.moment_deadline, it.deadline)
            },
            category = NetworkConstants.MomentCategory.getCategory(it.category),
            isPublic = true,
            isOwner = it.isOwner,
            traceCnt = context.getString(R.string.moment_user_trace_count, it.traceCnt),
            comment = it.comment,
            isExpired = it.deadline == -1
        )
    }
)