package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serial

/**
 * 모먼트 생성 요청
 */
@Parcelize
data class RequestMomentCreate(
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

    @SerializedName("isAnonymous")
    val isAnonymous: Boolean = false
) : Parcelable