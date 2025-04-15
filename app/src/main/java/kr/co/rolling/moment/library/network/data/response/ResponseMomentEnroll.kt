package kr.co.rolling.moment.library.network.data.response

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.network.NetworkConstants

@Parcelize
data class ResponseMomentEnroll(
    @SerializedName("deadline")
    val deadline: Int,             // 마감일까지 남은 일 수

    @SerializedName("category")
    val category: String,          // 카테고리 ENUM 값 (예: ANNIVERSARY)

    @SerializedName("title")
    val title: String,             // 제목

    @SerializedName("comment")
    val comment: String,           // 설명

    @SerializedName("coverImgUrl")
    val coverImgUrl: String,       // 커버 이미지 URL
) : Parcelable

@Parcelize
data class MomentEnrollInfo(
    val deadline: String = "",

    val category: NetworkConstants.MomentCategory? = null,

    val title: String = "",

    val comment: String = "",

    val coverImageUrl: String = "",

    val isExpired: Boolean = false
) : Parcelable

fun ResponseMomentEnroll.toEntity(context: Context) = MomentEnrollInfo(
    deadline = if (this.deadline == -1) context.getString(R.string.moment_deadline_expired) else context.getString(R.string.moment_deadline, this.deadline),
    category = NetworkConstants.MomentCategory.getCategory(this.category),
    title = this.title,
    comment = this.comment,
    coverImageUrl = this.coverImgUrl,
    isExpired = this.deadline == -1
)