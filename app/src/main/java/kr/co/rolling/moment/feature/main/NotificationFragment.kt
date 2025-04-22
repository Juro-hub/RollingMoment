package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentNotificationBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.data.response.PushItem
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MainViewModel
import kr.co.rolling.moment.library.util.navigate
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import timber.log.Timber

/**
 * 알림함
 */
@AndroidEntryPoint
class NotificationFragment : BaseFragment(R.layout.fragment_notification) {
    private lateinit var binding: FragmentNotificationBinding
    private val viewModel by activityViewModels<MainViewModel>()

    private val adapter by lazy {
        NotificationAdapter()
    }

    override fun initViewBinding(view: View) {
        binding = FragmentNotificationBinding.bind(view)
        binding.layoutToolBar.ivBack.setOnSingleClickListener { finishFragment() }
        binding.layoutToolBar.tvToolbarTitle.text = getString(R.string.notification_title)

        binding.rvNotification.adapter = adapter
    }

    override fun observeViewModel() {
        viewModel.requestPushInfo()

        viewLifecycleOwner.observeEvent(viewModel.pushInfo, ::handlePushList)
    }

    private fun handlePushList(event: SingleEvent<List<PushItem>>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handlePushList: data = ${it}")

            adapter.submitList(it)
            adapter.setClickListener {
                navigate(it.navigateData)
            }

            binding.rvNotification.isVisible = it.isNotEmpty()
            binding.layoutEmpty.isVisible = it.isEmpty()
        }
    }
}