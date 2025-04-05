package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.TraceFontType

/**
 *
 */
@Parcelize
data class ResponseMomentInfo(
    @SerializedName("inviteCode")
    val inviteCode: String,

    @SerializedName("isExpired")
    val isExpired: Boolean,

    @SerializedName("deadline")
    val deadLine: Int,

    @SerializedName("category")
    val category: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("coverImage")
    val coverImage: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("period")
    val period: String,

    @SerializedName("isPublic")
    val isPublic: Boolean,

    @SerializedName("isMine")
    val isMine: Boolean,

    @SerializedName("traces")
    val traces: List<ResponseTraces>
) : Parcelable

@Parcelize
data class ResponseTraces(
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
    val date: String,

    @SerializedName("reactions")
    val reactions: List<ResponseReactions>
) : Parcelable

@Parcelize
data class ResponseReactions(
    @SerializedName("type")
    val type: String,

    @SerializedName("count")
    val count: Int,

    @SerializedName("isClick")
    val isClick: Boolean,
) : Parcelable

@Parcelize
data class MomentInfo(
    val inviteCode: String = "",

    val isExpired: Boolean = false,

    val deadLine: String = "",

    val coverImage: String = "",

    //TODO Category Enum
    val category: String = "",

    val title: String = "",

    val content: String = "",

    val period: String = "",

    val isPublic: Boolean = false,

    val isMine: Boolean = false,

    val traceList: List<TraceInfo>? = null
) : Parcelable

@Parcelize
data class TraceInfo(
    val code: String = "",

    val nickname: String = "",

    val content: String = "",

    val font: TraceFontType = TraceFontType.DEFAULT,

    val color: Constants.TraceBackgroundColor = Constants.TraceBackgroundColor.NONE,

    val alignment: Constants.TraceTextAlign = Constants.TraceTextAlign.LEFT_ALIGN,

    val date: String = "",

    val reactionList: List<ReactionInfo>? = null
) : Parcelable

@Parcelize
data class ReactionInfo(
    val type: String = "",

    val count: Int = 0,

    val isClick: Boolean = false,
) : Parcelable