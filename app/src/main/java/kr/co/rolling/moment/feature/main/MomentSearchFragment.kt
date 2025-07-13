package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentSearchBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_EXPIRED
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_OWNER
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.network.data.response.MomentListSearchInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import timber.log.Timber

/**
 *
 */
@AndroidEntryPoint
class MomentSearchFragment : BaseFragment(R.layout.fragment_moment_search) {
    private lateinit var binding: FragmentMomentSearchBinding
    private val viewModel by activityViewModels<MomentViewModel>()

    private val adapter by lazy {
        HomeExpiredAdapter()
    }

    private val recentAdapter by lazy {
        RecentSearchAdapter()
    }

    override fun initViewBinding(view: View) {
        super.initViewBinding(view)
        binding = FragmentMomentSearchBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observeEvent(viewModel.momentSearchList, ::handleMomentList)
    }

    private fun handleMomentList(event: SingleEvent<MomentListSearchInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentList: data = ${it}")

            binding.layoutEmpty.isVisible = it.momentList?.isEmpty() == true
            binding.layoutResult.isVisible = it.momentList?.isNotEmpty() == true
            binding.tvMomentCount.text = getString(R.string.moment_search_count_title, it.momentList?.size ?: 0)
            binding.tvSearchTitle.text = getString(R.string.moment_search_empty, binding.etSearch.getData())
            adapter.submitList(it.momentList)

            // 모먼트 조회된 경우
            if (it.momentList?.isNotEmpty() == true) {
                var list = preferenceManager.getRecentSearch().toMutableList()
                if (list.size >= 5) {
                    list = list.subList(1, list.size)
                }
                list.add(binding.etSearch.getData())
                val result = list.distinct()
                preferenceManager.setRecentSearch(result)
                recentAdapter.submitList(result)
            }
        }
    }

    private fun initUI() {
        binding.root.setOnSingleClickListener {
            binding.root.requestFocus()
            binding.etSearch.clearFocus()
        }
        binding.etSearch.setFocusListener { _, hasFocus ->
            if (hasFocus) {
                val list = preferenceManager.getRecentSearch()
                binding.layoutRecentSearch.isVisible = list.isNotEmpty()
            } else {
                binding.layoutRecentSearch.hide()
            }
        }

        binding.tvRecentDelete.setOnSingleClickListener {
            preferenceManager.setRecentSearch(listOf())
        }

        binding.etSearch.setActionDoneListener {
            viewModel.requestSearch(it)
        }

        binding.etSearch.setIconClickListener {
            viewModel.requestSearch(binding.etSearch.getData())
        }

        binding.rvMomentList.adapter = adapter
        binding.rvMomentList.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_24), horizontalMargin = 0, spanCount = 1))
        adapter.setInfoClickListener {
            moveToMomentInfo(it)
        }

        adapter.setClickListener {
            moveToMomentDetail(it)
        }

        binding.rvRecent.adapter = recentAdapter
        recentAdapter.submitList(preferenceManager.getRecentSearch().distinct())

        recentAdapter.setClickListener {
            viewModel.requestSearch(it)
        }

        recentAdapter.setDelete { delete ->
            val list = preferenceManager.getRecentSearch().filter {
                it != delete
            }
            preferenceManager.setRecentSearch(list)
            recentAdapter.submitList(list)
            binding.layoutRecentSearch.isVisible = list.isNotEmpty()
        }
    }

    private fun moveToMomentInfo(info: MomentInfo) {
        val bottomSheet = MomentEditBottomSheetFragment()
        bottomSheet.arguments = bundleOf(NAVIGATION_KEY_MOMENT_CODE to info.code, NAVIGATION_KEY_IS_EXPIRED to info.isExpired, NAVIGATION_KEY_IS_OWNER to info.isOwner)
        bottomSheet.show(parentFragmentManager, "MomentEditBottomSheet")
    }

    private fun moveToMomentDetail(info: MomentInfo) {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)

        if (info.isOwner) {
            navController.navigate(R.id.MomentDetailFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to info.code))
        } else {
            navController.navigate(R.id.MomentEnrollFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to info.code))
        }
    }
}