package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentHomeBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.show

/**
 * Home 화면
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    private val momentAdapter by lazy {
        HomeIngMomentAdapter()
    }

    private val expiredMomentAdapter by lazy {
        HomeExpiredAdapter()
    }

    val dummyMomentList = listOf(
        MomentInfo(
            inviteCode = "INV001",
            isExpired = false,
            deadLine = "2025-08-01",
            coverImage = "https://picsum.photos/300/200?random=1",
            category = "FOOD",
            title = "맛집 탐방기",
            content = "서울의 숨겨진 맛집을 찾아다녔던 순간들",
            period = "2025.07.01 ~ 2025.07.10",
            isPublic = true,
            isMine = true
        ),
        MomentInfo(
            inviteCode = "INV002",
            isExpired = true,
            deadLine = "2024-12-31",
            coverImage = "https://picsum.photos/300/200?random=2",
            category = "DAILY",
            title = "하루하루 기록",
            content = "2024년의 평범한 일상 속 특별한 기록들",
            period = "2024.01.01 ~ 2024.12.31",
            isPublic = false,
            isMine = true
        ),
        MomentInfo(
            inviteCode = "INV003",
            isExpired = false,
            deadLine = "2025-11-15",
            coverImage = "https://picsum.photos/300/200?random=3",
            category = "TRAVEL",
            title = "강릉 당일치기",
            content = "강릉에서 먹고 보고 즐긴 하루",
            period = "2025.11.14",
            isPublic = true,
            isMine = false
        )
    )


    override fun initViewBinding(view: View) {
        binding = FragmentHomeBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    private fun initUI() {
        binding.rvHomeIngMoment.show()
        binding.rvHomeExpiredMoment.hide()

        binding.rvHomeIngMoment.adapter = momentAdapter
        binding.rvHomeIngMoment.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), horizontalMargin = 0, spanCount = 1))
        momentAdapter.setClickListener {
            //TODO 화면 이동
        }
        momentAdapter.setInfoClickListener {
            findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToMomentEditBottomSheetFragment(it.isExpired, it.inviteCode))
        }

        binding.rvHomeExpiredMoment.adapter = expiredMomentAdapter
        binding.rvHomeExpiredMoment.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_24), horizontalMargin = 0, spanCount = 1))
        expiredMomentAdapter.setClickListener {

        }
        expiredMomentAdapter.setInfoClickListener {
            findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToMomentEditBottomSheetFragment(it.isExpired, it.inviteCode))
        }

        setFragmentResultListener(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT) { _, bundle ->
            val code = bundle.getString(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_CODE)
            val isEdit = bundle.getBoolean(MomentEditBottomSheetFragment.BUNDLE_KEY_EDIT_TYPE)
        }

        val textAdapter = HomeProcessAdapter()
        textAdapter.setClickListener { process ->
            val isProcess = process == getString(R.string.process_ing)
            // 진행중
            binding.rvHomeIngMoment.isVisible = isProcess
            binding.rvHomeExpiredMoment.isVisible = !isProcess

            val list = dummyMomentList.filter {
                it.isExpired == !isProcess
            }

            momentAdapter.submitList(list)
            expiredMomentAdapter.submitList(list)
        }

        binding.rvProcess.adapter = textAdapter
        binding.rvProcess.addItemDecoration(CommonGridItemDecorator(verticalMargin = 0, horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_28), spanCount = 2))
        binding.layoutMomentIng.isVisible = dummyMomentList.isNotEmpty()
        binding.layoutEmpty.isVisible = dummyMomentList.isEmpty()
        textAdapter.submitList(listOf(getString(R.string.process_ing), getString(R.string.process_end)))
        momentAdapter.submitList(dummyMomentList.filter {
            !it.isExpired
        })
    }
}