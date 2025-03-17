package kr.co.rolling.moment.feature.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 앱 Base Activity
 */
abstract class BaseActivity : AppCompatActivity(){
    /** 화면 초기화 정의 */
    protected abstract fun initViewBinding()

    /** ViewModel observe 설정 */
    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }
}