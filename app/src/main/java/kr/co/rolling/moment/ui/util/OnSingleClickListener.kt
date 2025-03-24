package kr.co.rolling.moment.ui.util

import android.os.SystemClock
import android.view.View

/**
 * Single 클릭에 대한 처리 리스너
 *
 * 더블 클릭시 첫 클릭 이후 Call에 대한 처리 Skip (600ms)
 */
class OnSingleClickListener(
    private var interval: Int = 600,
    private var onSingleClick: (View) -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) {
            return
        }
        lastClickTime = elapsedRealtime
        onSingleClick(v)
    }

}