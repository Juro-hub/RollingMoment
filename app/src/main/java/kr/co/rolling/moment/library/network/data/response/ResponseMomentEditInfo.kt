package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.library.network.NetworkConstants

@Parcelize
data class ResponseMomentEditInfo(
    @SerializedName("title")
    val title: String,

    @SerializedName("expireType")
    val expireType: Int,

    @SerializedName("category")
    val category: String,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("isPublic")
    val isPublic: Boolean,

    @SerializedName("coverImgUrl")
    val coverImgUrl: String,

    @SerializedName("coverImgId")
    val coverImageId: Int
) : Parcelable

@Parcelize
data class MomentEditInfo(
    val title: String = "",

    val expireType: NetworkConstants.MomentExpireType = NetworkConstants.MomentExpireType.SEVEN_DAYS_LATER,

    val category: NetworkConstants.MomentCategory? = null,

    val comment: String = "",

    val isPublic: Boolean = false,

    val coverImage: MomentImageInfo? = null
) : Parcelable

fun ResponseMomentEditInfo.toEntity() = MomentEditInfo(
    title = title,
    expireType = NetworkConstants.MomentExpireType.getType(expireType.toString()),
    category = NetworkConstants.MomentCategory.getCategory(category),
    comment = comment,
    isPublic = isPublic,
    coverImage = MomentImageInfo(
        url = coverImgUrl,
        code = coverImageId.toString()
    )
)