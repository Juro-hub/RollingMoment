package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMainBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants.MOMENT_PUSH_DATA_KEY
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.data.NavigationData
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.navigate
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * Main 화면
 */
@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding

    /** 현재 노출되고 있는 Tab */
    private var currentTab = Tab.HOME

    override fun handleBackPressed() {
        if (binding.bottomNavView.selectedItemId == R.id.HomeFragment) {
            requireActivity().finishAffinity()
        } else {
            binding.bottomNavView.selectedItemId = R.id.HomeFragment
        }
    }

    override fun initViewBinding(view: View) {
        binding = FragmentMainBinding.bind(view)

        val navController = childFragmentManager.findFragmentById(R.id.main_host_fragment)
            ?.findNavController() ?: return

        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                Tab.HOME.navId -> currentTab = Tab.HOME
                Tab.SEARCH.navId -> currentTab = Tab.SEARCH
                Tab.MY_INFO.navId -> currentTab = Tab.MY_INFO
            }
        }

        binding.ivAlarm.setOnSingleClickListener {
            findNavController().navigateSafe(MainFragmentDirections.actionMainFragmentToNotificationFragment())
        }

        val momentCode = preferenceManager.getMomentCode()
        if (momentCode.isNotEmpty()) {
            preferenceManager.setMomentCode("")
            moveToMomentDetail(momentCode)
        }
        requireActivity().intent.extras?.let {
            val data = it.getParcelableCompat(MOMENT_PUSH_DATA_KEY, NavigationData::class.java) ?: return
            // 처리 완료 후 Key 제거
            requireActivity().intent.removeExtra(MOMENT_PUSH_DATA_KEY)
            navigate(data)
        }

        // App 링크로 수신할 경우
        requireActivity().intent.data?.let { uri ->
            val momentCode = uri.getQueryParameter(NAVIGATION_KEY_MOMENT_CODE) ?: return
            // 처리 완료 후 intent 초기화
            requireActivity().intent.data = null
            moveToMomentDetail(momentCode)
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    enum class Tab(@IdRes val navId: Int) {
        /** 홈탭 */
        HOME(R.id.HomeFragment),

        /** 검색 탭 */
        SEARCH(R.id.SearchFragment),

        /** 내정보 탭 */
        MY_INFO(R.id.MyInfoFragment),
    }

    private fun moveToMomentDetail(momentCode: String) {
        val controller = requireActivity().findNavController(R.id.nav_host_fragment)
        controller.navigate(R.id.MomentDetailFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to momentCode))
    }
}