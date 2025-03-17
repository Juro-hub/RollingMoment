package kr.co.rolling.moment.feature.intro.ui

import kr.co.rolling.moment.databinding.ActivityIntroBinding
import kr.co.rolling.moment.feature.base.BaseActivity

/**
 * Single Activity
 */
class MomentActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding
    override fun initViewBinding() {
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeViewModel() {
    }
}