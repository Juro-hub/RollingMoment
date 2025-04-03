package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentDetailBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.library.network.data.response.ReactionInfo
import kr.co.rolling.moment.library.network.data.response.TraceInfo
import kr.co.rolling.moment.ui.util.showExpandableText

/**
 * 모먼트 상세 조회 화면
 */
class MomentDetailFragment : BaseFragment(R.layout.fragment_moment_detail) {
    private lateinit var binding: FragmentMomentDetailBinding

    val dummyReactionList = listOf(
        ReactionInfo(type = "like", count = 10, isClick = false),
        ReactionInfo(type = "love", count = 3, isClick = true)
    )

    val dummyTraceList = listOf(
        TraceInfo(
            code = "trace001",
            nickname = "홍길동",
            content = "작성한 내용이 노출되는 영역 이것은 임시 조각 디자인입니다 최종 아니에요",
            font = TraceFontType.DEFAULT,
            color = Constants.TraceBackgroundColor.YELLOW,
            alignment = Constants.TraceTextAlign.CENTER_ALIGN,
            date = "2025-04-01",
            reactionList = dummyReactionList
        ),
        TraceInfo(
            code = "trace002",
            nickname = "이몽룡",
            content = "그 어느날 너와 내가 심하게 싸운 그날 이후로 너와 내 친구는 연락도 없고 날 피하는 것 같아 그제서야 난 느낀거야 모든것이 잘못 되있는걸 너와 내 친구는 어느새 다정한 연인이 되있었지",
            font = TraceFontType.LEAF,
            color = Constants.TraceBackgroundColor.BLUE,
            alignment = Constants.TraceTextAlign.RIGHT_ALIGN,
            date = "2025-04-02",
            reactionList = null
        )
    )

    val dummyMoment = MomentInfo(
        inviteCode = "ABC123XYZ",
        isExpired = "false",
        coverImage = "https://media.istockphoto.com/id/1482199015/ko/%EC%82%AC%EC%A7%84/%ED%96%89%EB%B3%B5%ED%95%9C-%EA%B0%95%EC%95%84%EC%A7%80-%EC%9B%A8%EC%9D%BC%EC%8A%A4-%EC%96%B4-%EC%BD%94%EA%B8%B0-14-%EC%A3%BC%EB%A0%B9-%EA%B0%9C%EA%B0%80-%EC%9C%99%ED%81%AC%ED%95%98%EA%B3%A0-%ED%97%90%EB%96%A1%EC%9D%B4%EA%B3%A0-%ED%9D%B0%EC%83%89%EC%97%90-%EA%B3%A0%EB%A6%BD%EB%90%98%EC%96%B4-%EC%95%89%EC%95%84-%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4.jpg?s=612x612&w=0&k=20&c=vW29tbABUS2fEJvPi8gopZupfTKErCDMfeq5rrOaAME=",
        deadLine = "D-7", // UNIX 타임스탬프 예시
        category = "여행",
        title = "제주도 여행 기록",
        content = "함께한 소중한 순간들 그대와 함께라면 나는 어디든 갈 수 있을지 알았는데 그건 아니였던것같아 함께한 소중한 순간들 그대와 함께라면 나는 어디든 갈 수 있을지 알았는데 그건 아니였던것같아함께한 소중한 순간들 그대와 함께라면 나는 어디든 갈 수 있을지 알았는데 그건 아니였던것같아",
        period = "2025.03.01 ~ 2025.03.05",
        isPublic = true,
        isMine = true,
        traceList = dummyTraceList
    )


    override fun initViewBinding(view: View) {
        binding = FragmentMomentDetailBinding.bind(view)

        initUI()
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {

        Glide.with(requireContext())
            .load(dummyMoment.coverImage)
            .fitCenter()
            .into(binding.ivImage)

        binding.tvDeadline.text = dummyMoment.deadLine
        binding.tvCategory.text = dummyMoment.category
        binding.tvMomentTitle.text = dummyMoment.title
        binding.tvContent.showExpandableText(
            dummyMoment.content
        )
        binding.tvDate.text = dummyMoment.period

        binding.layoutEmpty.isVisible = dummyMoment.traceList.isNullOrEmpty()
        binding.layoutTrace.isVisible = dummyMoment.traceList?.isNotEmpty() == true

        binding.tvTraceCount.text = getString(R.string.moment_detail_trace_count, dummyMoment.traceList?.size ?: 0)

        val adapter = MomentDetailTraceAdapter()
        adapter.submitList(dummyTraceList)
        binding.rvTrace.adapter = adapter
    }
}