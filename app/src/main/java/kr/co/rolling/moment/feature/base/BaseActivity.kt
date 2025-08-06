package kr.co.rolling.moment.feature.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 앱 Base Activity
 */
abstract class BaseActivity : AppCompatActivity() {
    /** 화면 초기화 정의 */
    protected abstract fun initViewBinding()

    /** ViewModel observe 설정 */
    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
        applyEdgeToEdgeInsets()
    }

    @SuppressLint("ResourceType")
    private fun applyEdgeToEdgeInsets() {
        val contentView = findViewById<View>(android.R.id.content)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
                        or WindowInsetsCompat.Type.ime()
            )

            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                bars.bottom
            )

            WindowInsetsCompat.CONSUMED
        }
    }
}