package kr.co.rolling.moment.library.network.data.response

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.library.network.NetworkConstants

@Parcelize
data class ResponseMomentDetail(
    @SerializedName("inviteCode")
    val inviteCode: String?, // not null 여부는 서버 정책에 따라

    @SerializedName("isExpired")
    val isExpired: Boolean,

    @SerializedName("deadline")
    val deadline: Int,

    @SerializedName("category")
    val category: String,

    @SerializedName("coverImgUrl")
    val coverImgUrl: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("period")
    val period: String,

    @SerializedName("isPublic")
    val isPublic: Boolean,

    @SerializedName("isOwner")
    val isOwner: Boolean,

    @SerializedName("traces")
    val traces: List<ResponseTrace>
) : Parcelable

@Parcelize
data class ResponseTrace(
    @SerializedName("code")
    val code: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("font")
    val font: String,

    @SerializedName("color")
    val color: String,

    @SerializedName("alignment")
    val alignment: String,

    @SerializedName("date")
    val date: String, // yyyy.MM.dd HH:mm,

    @SerializedName("textColor")
    val textColor : String? = null,

    @SerializedName("owner")
    val isOwner: Boolean? = null,

    @SerializedName("reactions")
    val reactions: List<ResponseReaction>
) : Parcelable

@Parcelize
data class ResponseReaction(
    @SerializedName("count")
    val count: Int,   // 리액션 개수

    @SerializedName("isClicked")
    val isClicked: Boolean // 본인이 눌렀는지 여부
) : Parcelable


@Parcelize
data class MomentDetailInfo(
    val inviteCode: String = "",

    val isExpired: Boolean = false,

    val deadline: String = "",

    val category: NetworkConstants.MomentCategory? = null,

    val coverImageUrl: String = "",

    val title: String = "",

    val content: String = "",

    val period: String = "",

    val isPublic: Boolean = false,

    val isOwner: Boolean = false,

    val traces: List<MomentTraceInfo>? = null,
) : Parcelable

@Parcelize
data class MomentTraceInfo(
    val code: String = "",

    val nickname: String = "",

    val content: String = "",

    val font: TraceFontType = TraceFontType.DEFAULT,

    val color: Constants.TraceBackgroundColor = Constants.TraceBackgroundColor.NONE,

    val alignment: Constants.TraceTextAlign = Constants.TraceTextAlign.LEFT_ALIGN,

    val date: String = "",

    val textColor: Constants.TraceTextColor = Constants.TraceTextColor.BLACK,

    val isOwner: Boolean = false,

    val reactions: List<ReactionInfo>? = null
) : Parcelable

@Parcelize
data class ReactionInfo(
    val count: Int = 0,

    val isClicked: Boolean = false
) : Parcelable

fun ResponseMomentDetail.toEntity(context: Context) = MomentDetailInfo(
    inviteCode = inviteCode.orEmpty(),
    isExpired = isExpired,
    deadline = if (deadline == -1) context.getString(R.string.moment_deadline_expired) else context.getString(R.string.moment_deadline, deadline),
    category = NetworkConstants.MomentCategory.getCategory(category),
    coverImageUrl = this.coverImgUrl,
    title = title,
    content = comment,
    period = period,
    isPublic = isPublic,
    isOwner = isOwner,
    traces = traces.map { it.toEntity() }
)

fun ResponseTrace.toEntity() = MomentTraceInfo(
    code = code,
    nickname = nickname,
    content = content,
    font = TraceFontType.getFontWithType(font),
    color = Constants.TraceBackgroundColor.getColor(color),
    alignment = Constants.TraceTextAlign.getAlign(alignment),
    date = date,
    textColor = Constants.TraceTextColor.getColor(textColor ?: ""),
    isOwner = isOwner == true,
    reactions = reactions.map { it.toEntity() }
)

fun ResponseReaction.toEntity() = ReactionInfo(
    count = count,
    isClicked = isClicked
)
