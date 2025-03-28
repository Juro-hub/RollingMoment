package kr.co.rolling.moment.ui.util

import android.animation.ObjectAnimator
import android.app.Service
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import kr.co.rolling.moment.ui.component.CommonDialog
import kr.co.rolling.moment.ui.component.CommonDialogData
import kotlin.math.abs

/**
 * SingleClickListener 을 설정한다.
 * 단, 버튼에 한하여 사용.
 * SwitchCompat같은 뷰에서 사용할 경우 액션에 대한 처리가 안되는 문제가 발생.
 *
 * @param onSingleClick ClickListener
 */
fun View.setOnSingleClickListener(onSingleClick: ((View) -> Unit)? = null) {
    val singleClick = OnSingleClickListener {
        onSingleClick?.invoke(it)
    }
    setOnClickListener(singleClick)
}

/**
 * 키보드를 보여주며, Focus 를 한다.
 *
 * @param delayMilis 포커스 이후 얼마의 ms 후에 키보드 노출하는 값
 */
fun View.focusAndShowKeyboard(delayMilis: Long = 100) {

    this.requestFocus()

    this.postDelayed({
        (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, 0)
    }, delayMilis)
}

/**
 * 키보드를 숨긴다.
 */
fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * ScrollView 을 특정 View 까지 Scroll 한다.
 *
 * @param view 기준이 될 View
 * @param marginTop 상단 여백 값
 * @param maxDuration 몇 MS 간 이동할지
 * @param onEnd 종료 후 Action
 */
fun NestedScrollView.smoothScrollToView(
    view: View,
    marginTop: Int = 0,
    maxDuration: Long = 500L,
    onEnd: () -> Unit = {}
) {
    if (this.getChildAt(0).height <= this.height) {
        onEnd()
        return
    }
    val y = computeDistanceToView(view) - marginTop
    val ratio = abs(y - this.scrollY) / (this.getChildAt(0).height - this.height).toFloat()
    ObjectAnimator.ofInt(this, "scrollY", y).apply {
        duration = (maxDuration * ratio).toLong()
        interpolator = AccelerateDecelerateInterpolator()
        doOnEnd {
            onEnd()
        }
        start()
    }
}

/**
 * ScrollView 와 View 사이의 최종 거리를 획득
 *
 * @param view 특정 View
 * @return View 와 scrollView 의 거리
 */
private fun NestedScrollView.computeDistanceToView(view: View): Int {
    return abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top))
}

/**
 * 화면의 절대 좌표를 구한다.
 *
 * @param view 절대 좌표 구할 View
 * @return View 의 절대 좌표
 */
private fun calculateRectOnScreen(view: View): Rect {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    return Rect(
        location[0],
        location[1],
        location[0] + view.measuredWidth,
        location[1] + view.measuredHeight
    )
}

/**
 * View 를 VISIBLE 처리한다.
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * View 를 INVISIBLE 처리한다.
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * View 를 GONE 처리한다.
 */
fun View.hide() {
    visibility = View.GONE
}

fun Fragment.showDialog(
    dialogData: CommonDialogData, positiveCallback: (() -> Unit)? = null,
    negativeCallback: (() -> Unit)? = null,
) {
    val bundle = Bundle()
    bundle.putParcelable(CommonDialog.ARG_DIALOG_DATA, dialogData)

    val commonDialog = CommonDialog().apply {
        setPositiveListener(positiveCallback)
        setNegativeListener(negativeCallback)
    }

    commonDialog.arguments = bundle
    commonDialog.show(parentFragmentManager, "newAlert")
}

fun AppCompatActivity.showDialog(
    dialogData: CommonDialogData, positiveCallback: (() -> Unit)? = null,
    negativeCallback: (() -> Unit)? = null,
) {
    val bundle = Bundle()
    bundle.putParcelable(CommonDialog.ARG_DIALOG_DATA, dialogData)

    val commonDialog = CommonDialog().apply {
        setPositiveListener(positiveCallback)
        setNegativeListener(negativeCallback)
    }

    commonDialog.arguments = bundle
    commonDialog.show(supportFragmentManager, "newAlert")
}