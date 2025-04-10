package kr.co.rolling.moment.library.network.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 모먼트 생성 조회 Data
 */
@Parcelize
data class ResponseMomentCreateInfo(
    @SerializedName("categories")
    val category: List<ResponseMomentCreateCategory>,

    @SerializedName("coverImages")
    val coverImage: List<ResponseMomentCreateCoverImage>
) : Parcelable

/**
 * 모먼트 생성 카테고리
 */
@Parcelize
data class ResponseMomentCreateCategory(
    @SerializedName("code")
    val code: String,

    @SerializedName("title")
    val title: String
) : Parcelable

/**
 * 모먼트 생성 커버 이미지
 */
@Parcelize
data class ResponseMomentCreateCoverImage(
    @SerializedName("id")
    val coverImgId: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("type")
    val type: String
) : Parcelable

@Parcelize
data class MomentCreateInfo(
    val categoryList: List<MomentCreateCategoryInfo>? = null,

    val coverImageList: List<MomentCreateCoverImageInfo>? = null
) : Parcelable

/**
 * 모먼트 생성 카테고리
 */
@Parcelize
data class MomentCreateCategoryInfo(
    val code: String,

    val title: String
) : Parcelable

/**
 * 모먼트 생성 커버 이미지
 */
@Parcelize
data class MomentCreateCoverImageInfo(
    val imageInfo: List<MomentImageInfo>,
    val type: String
) : Parcelable

/**
 * 모먼트 생성 이미지
 */
@Parcelize
data class MomentImageInfo(
    val url: String,
    val code: String
) : Parcelable

fun ResponseMomentCreateInfo.toEntity() = MomentCreateInfo(
    categoryList = this.category.map {
        MomentCreateCategoryInfo(
            code = it.code,
            title = it.title
        )
    },

    coverImageList = this.coverImage
        .groupBy { it.type } // 카테고리 기준으로 그룹핑
        .map { (category, items) ->
            MomentCreateCoverImageInfo(
                imageInfo = items.map {
                    MomentImageInfo(
                        url = it.url,
                        code = it.coverImgId
                    )
                },
                type = category
            )
        }
)
