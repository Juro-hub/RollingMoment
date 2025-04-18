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
data class ResponseMomentList(
    @SerializedName("moments")
    val momentList: List<ResponseMoment>
) : Parcelable

@Parcelize
data class ResponseMoment(
    @SerializedName("code")
    val code: String = "",

    @SerializedName("coverImgUrl")
    val coverImgUrl: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("comment")
    val comment: String = "",

    @SerializedName("deadline")
    val deadline: Int = -1,

    @SerializedName("category")
    val category: String = "",

    @SerializedName("isPublic")
    val isPublic: Boolean = true,

    @SerializedName("isOwner")
    val isOwner: Boolean,

    @SerializedName("traceCnt")
    val traceCnt: Int = -1,
) : Parcelable

@Parcelize
data class MomentListInfo(
    val momentList: List<MomentInfo>? = null
) : Parcelable

@Parcelize
data class MomentInfo(
    val code: String = "",

    val coverImgUrl: String = "",

    val title: String = "",

    val comment: String = "",

    val deadline: String = "",

    val category: NetworkConstants.MomentCategory? = null,

    val isPublic: Boolean = true,

    val isOwner: Boolean = false,

    val traceCnt: String = "",

    val isExpired: Boolean = false
) : Parcelable

fun ResponseMomentList.toEntity(context: Context) = MomentListInfo(
    momentList = this.momentList.map {
        MomentInfo(
            code = it.code,
            coverImgUrl = it.coverImgUrl,
            title = it.title,
            deadline = if (it.deadline == -1) context.getString(R.string.moment_deadline_expired) else context.getString(R.string.moment_deadline, it.deadline),
            category = NetworkConstants.MomentCategory.getCategory(it.category),
            isPublic = true,
            isOwner = it.isOwner,
            traceCnt = context.getString(R.string.moment_user_trace_count, it.traceCnt),
            comment = it.comment,
            isExpired = it.deadline == -1
        )
    }
)