package kr.co.rolling.moment.library.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Grid ItemRecyclerView에 대한 divider Decoration 처리
 */
class CommonGridItemDecorator(
    private val verticalMargin: Int,
    private val horizontalMargin: Int,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition

        outRect.bottom = 0
        outRect.top = if (itemPosition / spanCount > 0) {
            // 2행 이상
            verticalMargin
        } else {
            // 1행
            0
        }

        when {
            // 맨 왼쪽 열
            itemPosition % spanCount == 0 -> {
                outRect.left = 0
                outRect.right = horizontalMargin / 2
            }

            // 맨 오른쪽 열
            (itemPosition + 1) % spanCount == 0 -> {
                outRect.left = horizontalMargin / 2
                outRect.right = 0
            }

            else -> {
                outRect.left = horizontalMargin / 2
                outRect.right = horizontalMargin / 2
            }
        }
    }
}