package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.network.NetworkConstants

@Parcelize
data class ResponseMomentSimpleInfo(
    @SerializedName("title")
    val title: String,

    @SerializedName("deadline")
    val deadline: Int,

    @SerializedName("category")
    val category: String,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("isPublic")
    val isPublic: Boolean,

    @SerializedName("coverImgUrl")
    val coverImgUrl: String,

    @SerializedName("coverImgId")
    val coverImageId: Int,

    @SerializedName("inviteUrl")
    val inviteUrl: String,

    @SerializedName("period")
    val period: String,

    @SerializedName("inviteImgUrl")
    val inviteImgUrl: String
) : Parcelable

@Parcelize
data class MomentSimpleInfo(
    val title: String = "",

    val expireType: NetworkConstants.MomentExpireType = NetworkConstants.MomentExpireType.SEVEN_DAYS_LATER,

    val category: NetworkConstants.MomentCategory? = null,

    val comment: String = "",

    val isPublic: Boolean = false,

    val coverImage: MomentImageInfo? = null,

    val inviteUrl: String = "",

    val period: String = "",

    val isExpired: Boolean = false,

    val inviteImgUrl: String = ""
) : Parcelable

fun ResponseMomentSimpleInfo.toEntity() = MomentSimpleInfo(
    title = title,
    expireType = NetworkConstants.MomentExpireType.getType(deadline.toString()),
    category = NetworkConstants.MomentCategory.getCategory(category),
    comment = comment,
    isPublic = isPublic,
    coverImage = MomentImageInfo(
        url = coverImgUrl,
        code = coverImageId.toString()
    ),
    inviteImgUrl = inviteImgUrl,
    period = period,
    inviteUrl = inviteUrl,
    isExpired = deadline == -1
)