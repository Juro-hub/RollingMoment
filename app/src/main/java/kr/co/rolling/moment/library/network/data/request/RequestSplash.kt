package kr.co.rolling.moment.library.network.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Splash
 */
@Parcelize
data class RequestSplash(
    @SerializedName("pushToken")
    val pushToken: String
) : Parcelable