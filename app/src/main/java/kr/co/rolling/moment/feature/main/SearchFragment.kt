package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.BuildConfig
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSearchBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_EXPIRED
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_OWNER
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.network.data.response.MomentListInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.library.util.showToast
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.show
import timber.log.Timber

/**
 * 탐색 탭 UI
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private val filterAdapter by lazy {
        FilterAdapter()
    }

    private val viewModel by activityViewModels<MomentViewModel>()

    private val adapter by lazy {
        HomeExpiredAdapter()
    }

    private val popularAdapter by lazy {
        PopularMomentAdapter()
    }

    private lateinit var binding: FragmentSearchBinding
    override fun initViewBinding(view: View) {
        binding = FragmentSearchBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        if (BuildConfig.ADMIN) {
            viewModel.requestMomentListAdmin()
        } else {
            viewModel.requestMomentList()
        }

        viewLifecycleOwner.observeEvent(viewModel.momentList, ::handleMomentList)
        viewLifecycleOwner.observeEvent(viewModel.momentDelete, ::handleMomentListDelete)
    }

    private fun handleMomentList(event: SingleEvent<MomentListInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentList: data = ${it}")

            binding.rvMomentList.isVisible = !it.momentList.isNullOrEmpty()
            binding.rvFilter.isVisible = !it.momentList.isNullOrEmpty()
            binding.layoutEmpty.isVisible = it.momentList.isNullOrEmpty()

            var list = it.momentList?.sortedByDescending { it.deadLine }
            val currentCategory = filterAdapter.getCurrentCategory()
            list = if (currentCategory != Constants.MomentCategory.ALL) {
                list?.filter { it.category?.code == currentCategory.code }
            } else {
                list
            }
            adapter.submitList(list)
            popularAdapter.submitList(it.popularList)
        }
    }

    private fun handleMomentListDelete(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentList: data = ${it}")

            if (it) {
                showToast(getString(R.string.moment_detail_delete_done))
                viewModel.requestMomentList()
            }
        }
    }

    private fun initUI() {
        binding.rvFilter.adapter = filterAdapter
        filterAdapter.submitList(Constants.MomentCategory.entries.toList())
        binding.rvFilter.addItemDecoration(
            CommonGridItemDecorator(
                verticalMargin = 0,
                horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_8),
                spanCount = Constants.MomentCategory.entries.size,
                startMargin = resources.getDimensionPixelSize(R.dimen.spacing_20)
            )
        )
        filterAdapter.setClickListener { category ->
            val list = if (category == Constants.MomentCategory.ALL) {
                viewModel.getMomentList()
            } else {
                viewModel.getMomentList()?.filter { moment ->
                    moment.category?.code == category.code
                }
            }

            if (list.isNullOrEmpty()) {
                binding.rvMomentList.hide()
                binding.layoutEmpty.show()
                return@setClickListener
            }


            binding.layoutEmpty.hide()
            binding.rvMomentList.show()
            adapter.submitList(list.sortedByDescending { it.deadLine })
        }

        binding.rvMomentList.adapter = adapter
        binding.rvMomentList.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_24), horizontalMargin = 0, spanCount = 1))

        binding.rvPopularMomentList.adapter = popularAdapter
        binding.rvPopularMomentList.addItemDecoration(
            CommonGridItemDecorator(
                verticalMargin = 0,
                horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_12),
                spanCount = 3,
                startMargin = resources.getDimensionPixelSize(R.dimen.spacing_20)
            )
        )

        val navController = requireActivity().findNavController(R.id.nav_host_fragment)

        setFragmentResultListener(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT) { _, bundle ->
            val code = bundle.getString(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_CODE) ?: return@setFragmentResultListener
            val isEdit = bundle.getBoolean(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_TYPE)

            if (isEdit) {
                navController.navigate(R.id.MomentCreateFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to code))
            } else {
                viewModel.requestMomentDelete(code)
            }
        }

        adapter.setInfoClickListener {
            moveToMomentInfo(it)
        }

        adapter.setClickListener {
            moveToMomentDetail(it)
        }

        popularAdapter.setInfoClickListener {
            moveToMomentInfo(it)
        }

        popularAdapter.setClickListener {
            moveToMomentDetail(it)
        }

        binding.etSearch.setClickListener {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.MomentSearchFragment)
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