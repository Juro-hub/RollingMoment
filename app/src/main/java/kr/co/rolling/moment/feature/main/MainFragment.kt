package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMainBinding
import kr.co.rolling.moment.feature.base.BaseFragment

/**
 *
 */
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

        binding.bottomNavView.selectedItemId = R.id.HomeFragment
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
}