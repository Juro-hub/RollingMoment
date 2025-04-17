package kr.co.rolling.moment.ui.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import androidx.core.graphics.createBitmap

class BorderTransformation(
    private val borderSize: Float,
    private val borderColor: Int,
    private val cornerRadius: Float
) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("rounded_border_transformation".toByteArray())
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val output = createBitmap(width, height)
        val canvas = Canvas(output)

        // 이미지 그리기
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(toTransform, 0f, 0f, null)

        // 외곽선 그리기
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderSize
        }

        val halfBorder = borderSize / 2f
        val rect = RectF(
            halfBorder,
            halfBorder,
            width - halfBorder,
            height - halfBorder
        )
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint)

        return output
    }
}
