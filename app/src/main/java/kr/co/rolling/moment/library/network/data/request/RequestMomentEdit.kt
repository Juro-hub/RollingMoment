package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 모먼트 생성 요청
 */
@Parcelize
data class RequestMomentEdit(
    @SerializedName("coverImgFileKey")
    val coverImgFileKey: String = "",

    @SerializedName("coverImgId")
    val coverImgId: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("expireType")
    val expireType: String = "",

    @SerializedName("category")
    val category: String = "",

    @SerializedName("comment")
    val comment: String = "",

    @SerializedName("isPublic")
    val isPublic: Boolean = false,

    @SerializedName("momentCode")
    val momentCode: String = "'"
) : Parcelable