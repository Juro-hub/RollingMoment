package kr.co.rolling.moment.ui.util

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager

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