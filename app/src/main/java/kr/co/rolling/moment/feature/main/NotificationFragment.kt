package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentNotificationBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.data.response.NotificationInfo
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 알림함
 */
@AndroidEntryPoint
class NotificationFragment : BaseFragment(R.layout.fragment_notification) {
    private lateinit var binding: FragmentNotificationBinding
    val dummyPushList = listOf(
        NotificationInfo(
            type = "event",
            content = "이벤트에 참여해보세요!",
            date = "2025-04-05",
            pageType = "eventDetail",
            navigationType = "push"
        ),
        NotificationInfo(
            type = "notice",
            content = "앱 이용약관이 변경되었습니다.",
            date = "2025-04-01",
            pageType = "noticeDetail",
            navigationType = "modal"
        ),
        NotificationInfo(
            type = "promotion",
            content = "봄맞이 프로모션! 최대 50% 할인",
            date = "2025-03-28",
            pageType = "promotionPage",
            navigationType = "web"
        )
    )

    private val adapter by lazy {
        NotificationAdapter()
    }

    override fun initViewBinding(view: View) {
        binding = FragmentNotificationBinding.bind(view)
        binding.layoutToolBar.ivBack.setOnSingleClickListener { finishFragment() }
        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.notification_title)

        binding.rvNotification.adapter = adapter
        adapter.submitList(dummyPushList)

        binding.rvNotification.isVisible = dummyPushList.isNotEmpty()
        binding.layoutEmpty.isVisible = dummyPushList.isEmpty()
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }
}