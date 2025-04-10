package kr.co.rolling.moment.library.network.data.response

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.network.NetworkConstants

/**
 *
 */
@Parcelize
data class ResponseHome(
    @SerializedName("hasAlarm")
    val hasAlarm: Boolean,

    @SerializedName("inProgressMomentList")
    val inProgressMomentList: List<ResponseHomeMoment>,

    @SerializedName("endedMomentList")
    val endedMomentList: List<ResponseHomeMoment>
) : Parcelable

@Parcelize
data class ResponseHomeMoment(
    @SerializedName("momentCode")
    val momentCode: String,

    @SerializedName("deadline")
    val deadline: Int,

    @SerializedName("category")
    val category: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("coverImgUrl")
    val coverImgUrl: String,

    @SerializedName("isOwner")
    val isOwner: Boolean,

    @SerializedName("isPublic")
    val isPublic: Boolean,

    @SerializedName("traceCnt")
    val traceCnt: Int
) : Parcelable

@Parcelize
data class HomeInfo(
    val hasAlarm: Boolean = false,

    val progressMoment: List<HomeMomentInfo>? = null,
    val expiredMoment: List<HomeMomentInfo>? = null,
) : Parcelable

@Parcelize
data class HomeMomentInfo(
    val momentCode: String = "",

    val deadline: String = "",

    val category: NetworkConstants.MomentCategory = NetworkConstants.MomentCategory.WEDDING,

    val title: String = "",

    val comment: String = "",

    val coverImageUrl: String = "",

    val isOwner: Boolean = false,

    val isPublic: Boolean = false,

    val traceCount: String = "",

    val isExpired: Boolean = false
) : Parcelable

fun HomeMomentInfo.toMomentInfo(): MomentInfo {
    return MomentInfo(
        code = momentCode,
        coverImgUrl = coverImageUrl,
        title = title,
        comment = comment,
        deadline = deadline,
        category = category,
        isPublic = isPublic,
        isOwner = isOwner,
        traceCnt = traceCount,
        isExpired = isExpired
    )
}


fun ResponseHome.toEntity(context: Context) = HomeInfo(
    hasAlarm = this.hasAlarm,
    progressMoment = this.inProgressMomentList.map {
        HomeMomentInfo(
            momentCode = it.momentCode,
            deadline = if (it.deadline == -1) context.getString(R.string.moment_deadline_expired) else context.getString(R.string.moment_deadline, it.deadline),
            category = NetworkConstants.MomentCategory.getCategory(it.category),
            title = it.title,
            comment = it.comment,
            coverImageUrl = it.coverImgUrl,
            isOwner = it.isOwner,
            isPublic = it.isPublic,
            traceCount = context.getString(R.string.moment_user_trace_count, it.traceCnt),
            isExpired = it.deadline == -1
        )
    }
)