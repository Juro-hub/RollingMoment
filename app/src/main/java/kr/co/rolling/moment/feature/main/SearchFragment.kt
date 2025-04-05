package kr.co.rolling.moment.feature.main

import android.view.View
import androidx.navigation.fragment.findNavController
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentSearchBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.navigateSafe

/**
 * 탐색 탭 UI
 */
class SearchFragment : BaseFragment(R.layout.fragment_search) {
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
            isMine = false
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

    private val adapter by lazy {
        HomeExpiredAdapter()
    }

    private lateinit var binding: FragmentSearchBinding
    override fun initViewBinding(view: View) {
        binding = FragmentSearchBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    private fun initUI() {
        binding.rvMomentList.adapter = adapter
        binding.rvMomentList.addItemDecoration(CommonGridItemDecorator(verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_24), horizontalMargin = 0, spanCount = 1))

        adapter.submitList(dummyMomentList)
        adapter.setClickListener {

        }
        adapter.setInfoClickListener {
            findNavController().navigateSafe(SearchFragmentDirections.actionSearchFragmentToMomentEditBottomSheetFragment(it.isExpired, it.inviteCode))
        }
    }
}