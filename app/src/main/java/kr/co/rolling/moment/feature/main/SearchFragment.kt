package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSearchBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_EXPIRED
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_OWNER
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.network.data.response.MomentListInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.observeEvent
import timber.log.Timber

/**
 * 탐색 탭 UI
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private val viewModel by activityViewModels<MomentViewModel>()
    private var clickItem = MomentInfo()

    private val adapter by lazy {
        HomeExpiredAdapter()
    }

    private lateinit var binding: FragmentSearchBinding
    override fun initViewBinding(view: View) {
        binding = FragmentSearchBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        viewModel.requestMomentList()

        viewLifecycleOwner.observeEvent(viewModel.momentList, ::handleMomentList)
        viewLifecycleOwner.observeEvent(viewModel.momentDelete, ::handleMomentListDelete)
    }

    private fun handleMomentList(event: SingleEvent<MomentListInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentList: data = ${it}")

            binding.rvMomentList.isVisible = !it.momentList.isNullOrEmpty()
            binding.layoutEmpty.isVisible = it.momentList.isNullOrEmpty()

            adapter.submitList(it.momentList)
        }
    }

    private fun handleMomentListDelete(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentList: data = ${it}")

            if (it) {
                viewModel.requestMomentList()
            }
        }
    }

    private fun initUI() {
        binding.rvMomentList.adapter = adapter
        binding.rvMomentList.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_24), horizontalMargin = 0, spanCount = 1))

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
            clickItem = MomentInfo(
                code = it.code,
                coverImgUrl = it.coverImgUrl,
                title = it.title,
                comment = it.comment,
                deadLineText = it.deadLineText,
                deadLine = it.deadLine,
                category = it.category,
                isPublic = it.isPublic,
                isOwner = it.isOwner,
                traceCnt = it.traceCnt,
                isExpired = it.isExpired
            )
            val bottomSheet = MomentEditBottomSheetFragment()
            bottomSheet.arguments = bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.code, NAVIGATION_KEY_IS_EXPIRED to it.isExpired, NAVIGATION_KEY_IS_OWNER to it.isOwner)
            bottomSheet.show(parentFragmentManager, "MomentEditBottomSheet")
        }

        adapter.setClickListener {
            if (it.isOwner) {
                navController.navigate(R.id.MomentDetailFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.code))
            } else {
                navController.navigate(R.id.MomentEnrollFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.code))
            }
        }
    }
}