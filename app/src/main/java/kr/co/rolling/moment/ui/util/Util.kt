package kr.co.rolling.moment.ui.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.data.Constants
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

fun Fragment.showPermissionDialog(
    type: Constants.PermissionType
) {
    var title: String
    var content: String

    when (type) {
        Constants.PermissionType.CAMERA_PERMISSION -> {
            title = getString(R.string.permission_title_camera)
            content = getString(R.string.permission_content_camera)
        }

        Constants.PermissionType.STORAGE_PERMISSION -> {
            title = getString(R.string.permission_title_storage)
            content = getString(R.string.permission_content_storage)
        }

        Constants.PermissionType.ALARM_PERMISSION -> {
            title = getString(R.string.permission_title_alarm)
            content = getString(R.string.permission_content_alarm)
        }
    }

    showDialog(CommonDialogData(title = title, contents = content, positiveText = getString(R.string.permission_button), negativeText = getString(R.string.clsoe)), positiveCallback = {
        startActivity(getPermissionSettingIntent())
    })
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

/**
 * 시스템 권한 설정 화면 Intent 정보 생성
 *
 * @return 시스템 권한 설정 Intent 정보
 */
fun Fragment.getPermissionSettingIntent(): Intent {
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireActivity().packageName}")).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
    }
}

@SuppressLint("SetTextI18n")
fun TextView.showExpandableText(
    fullText: String,
    maxLinesCollapsed: Int = 2,
) {
    // 초기 상태 설정
    this.text = fullText
    this.maxLines = Int.MAX_VALUE

    post {
        if (lineCount <= maxLinesCollapsed) return@post

        // 텍스트 줄이기
        val truncatedText = getTruncatedText(fullText, maxLinesCollapsed)
        val collapsedText = "$truncatedText..."

        this.text = collapsedText
        this.maxLines = maxLinesCollapsed
        this.setOnClickListener {
            this.text = "$fullText"
            this.maxLines = Int.MAX_VALUE

            this.setOnClickListener {
                this.text = collapsedText
                this.maxLines = maxLinesCollapsed
                this.showExpandableText(fullText, maxLinesCollapsed)
            }
        }
    }
}

// 내부에서 사용할 잘라진 텍스트 계산 함수
private fun TextView.getTruncatedText(text: String, maxLines: Int): String {
    val layout = layout ?: return text
    val endIndex = layout.getLineEnd(maxLines - 1).coerceAtMost(text.length)
    return text.substring(0, endIndex).trim()
}
