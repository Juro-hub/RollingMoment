package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentHomeBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_EXPIRED
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_IS_OWNER
import kr.co.rolling.moment.library.data.Constants.NAVIGATION_KEY_MOMENT_CODE
import kr.co.rolling.moment.library.network.data.response.HomeInfo
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MainViewModel
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.component.CommonDialogData
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import kr.co.rolling.moment.ui.util.showDialog

/**
 * Home 화면
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel by activityViewModels<MainViewModel>()
    private val momentViewModel by activityViewModels<MomentViewModel>()

    private val momentAdapter by lazy {
        HomeIngMomentAdapter()
    }

    private val expiredMomentAdapter by lazy {
        HomeExpiredAdapter()
    }

    override fun handleBackPressed() {
        requireActivity().finishAffinity()
    }

    override fun initViewBinding(view: View) {
        binding = FragmentHomeBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewLifecycleOwner.observeEvent(viewModel.homeInfo, ::handleHomeInfo)
        viewLifecycleOwner.observeEvent(momentViewModel.momentDelete, ::handleMomentDelete)

        viewModel.requestHomeInfo()
    }

    private fun handleHomeInfo(data: SingleEvent<HomeInfo>) {
        data.getContentIfNotHandled()?.let {
            binding.layoutMomentIng.isVisible = !it.progressMoment.isNullOrEmpty()
            binding.layoutEmpty.isVisible = it.progressMoment.isNullOrEmpty()
            momentAdapter.submitList(it.progressMoment)

            momentAdapter.setInfoClickListener {
                val bottomSheet = MomentEditBottomSheetFragment()
                bottomSheet.arguments = bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.momentCode, NAVIGATION_KEY_IS_EXPIRED to it.isExpired, NAVIGATION_KEY_IS_OWNER to it.isOwner)
                bottomSheet.show(parentFragmentManager, "MomentEditBottomSheet")
            }

            expiredMomentAdapter.submitList(it.expiredMoment)
            expiredMomentAdapter.setInfoClickListener {
                val bottomSheet = MomentEditBottomSheetFragment()
                bottomSheet.arguments = bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.code, NAVIGATION_KEY_IS_EXPIRED to it.isExpired, NAVIGATION_KEY_IS_OWNER to it.isOwner)
                bottomSheet.show(parentFragmentManager, "MomentEditBottomSheet")
            }
        }
    }

    private fun handleMomentDelete(data: SingleEvent<Boolean>) {
        data.getContentIfNotHandled()?.let {
            if (it) {
                viewModel.requestHomeInfo()
            }
        }
    }

    private fun initUI() {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        binding.rvHomeIngMoment.show()
        binding.rvHomeExpiredMoment.hide()

        binding.rvHomeIngMoment.adapter = momentAdapter
        binding.rvHomeIngMoment.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), horizontalMargin = 0, spanCount = 1))
        momentAdapter.setClickListener {
            navController.navigate(R.id.MomentDetailFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.momentCode))
        }

        binding.btnCreate.setOnSingleClickListener {
            navController.navigate(R.id.MomentCreateFragment)
        }
        binding.rvHomeExpiredMoment.adapter = expiredMomentAdapter
        binding.rvHomeExpiredMoment.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_24), horizontalMargin = 0, spanCount = 1))
        expiredMomentAdapter.setClickListener {
            navController.navigate(R.id.MomentDetailFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to it.code))
        }

        setFragmentResultListener(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT) { _, bundle ->
            val code = bundle.getString(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_CODE) ?: return@setFragmentResultListener
            val isEdit = bundle.getBoolean(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_TYPE)

            if (isEdit) {
                navController.navigate(R.id.MomentCreateFragment, bundleOf(NAVIGATION_KEY_MOMENT_CODE to code))
            } else {
                showDialog(
                    CommonDialogData(
                        title = getString(R.string.moment_delete_title),
                        contents = getString(R.string.moment_delete_description),
                        positiveText = getString(R.string.moment_delete_positive),
                        negativeText = getString(R.string.no)
                    ), positiveCallback = {
                        momentViewModel.requestMomentDelete(code)
                    })
            }
        }

        val textAdapter = HomeProcessAdapter()
        textAdapter.setClickListener { process ->
            val isProcess = process == getString(R.string.process_ing)

            // 진행중
            binding.rvHomeIngMoment.isVisible = isProcess
            binding.rvHomeExpiredMoment.isVisible = !isProcess

            val list = if (isProcess) {
                viewModel.getProgressMomentList()
            } else {
                viewModel.getExpiredMomentList()
            }

            if (list.isNullOrEmpty()) {
                binding.layoutEmpty.show()
                binding.layoutMomentIng.hide()
                if (isProcess) {
                    binding.ivEmpty.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.empty_img_01))
                    binding.tvEmptyTitle.text = getString(R.string.moment_empty_title)
                    binding.tvEmptyDescription.text = getString(R.string.moment_empty_description)
                } else {
                    binding.ivEmpty.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.empty_img_02))
                    binding.tvEmptyTitle.text = getString(R.string.moment_empty_title_expire)
                    binding.tvEmptyDescription.text = getString(R.string.moment_empty_description_expire)
                }
                return@setClickListener
            }

            binding.layoutEmpty.hide()
            binding.layoutMomentIng.show()

            if (isProcess) {
                momentAdapter.submitList(viewModel.getProgressMomentList())
            } else {
                val list = viewModel.getExpiredMomentList()?.toMutableList() ?: mutableListOf<MomentInfo>()
                expiredMomentAdapter.submitList(list)
            }
        }

        binding.rvProcess.adapter = textAdapter
        binding.rvProcess.addItemDecoration(CommonGridItemDecorator(verticalMargin = 0, horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_28), spanCount = 2))

        textAdapter.submitList(listOf(getString(R.string.process_ing), getString(R.string.process_end)))
    }
}