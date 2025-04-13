package kr.co.rolling.moment.feature.moment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import kr.co.rolling.moment.BuildConfig
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentResultBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.data.Constants.MOMENT_SHARE_KAKAO_WEB_KEY
import kr.co.rolling.moment.library.data.Constants.SCHEME_SHARE_TYPE
import kr.co.rolling.moment.library.data.Constants.SCHEME_SMS_TO
import kr.co.rolling.moment.library.data.Constants.SCHEME_SMS_TO_BODY
import kr.co.rolling.moment.library.network.data.response.MomentSimpleInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.CommonGridItemDecorator
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import timber.log.Timber


/**
 * 모먼트 결과 안내 화면
 */
class MomentResultFragment : BaseFragment(R.layout.fragment_moment_result) {
    private lateinit var binding: FragmentMomentResultBinding
    private val args by navArgs<MomentResultFragmentArgs>()

    private val viewModel by activityViewModels<MomentViewModel>()

    private val adapter by lazy {
        MomentShareAdapter()
    }

    override fun initViewBinding(view: View) {
        binding = FragmentMomentResultBinding.bind(view)

        val randomColor = Constants.TraceBackgroundColor.entries.random().color
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), randomColor))

        binding.layoutToolBar.ivBack.hide()
        binding.layoutToolBar.ivClose.show()
        binding.layoutToolBar.ivClose.setOnSingleClickListener {
            finishFragment()
        }

        val gridLayoutManager = GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)
        val data = Constants.MomentShareType.entries
        adapter.submitList(data.toMutableList())
        binding.rvAdapter.layoutManager = gridLayoutManager
        binding.rvAdapter.adapter = adapter
        binding.rvAdapter.addItemDecoration(CommonGridItemDecorator(verticalMargin = 0, horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_10), spanCount = 4))
        binding.tvEnd.text = getString(args.enterType.stringId)
    }

    override fun observeViewModel() {
        viewModel.requestMomentSimple(args.momentCode)

        viewLifecycleOwner.observeEvent(viewModel.momentSimpleInfo, ::handleMomentShareResult)
    }

    private fun handleMomentShareResult(event: SingleEvent<MomentSimpleInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentEnroll: data = ${it}")

            binding.layoutMoment.apply {
                ivMore.hide()
                tvDeadline.text = getString(it.expireType.textId)
                if (it.isExpired) {
                    tvDeadline.setBackgroundResource(R.drawable.shape_4_e7f5e7)
                    tvDeadline.setTextColor(requireContext().getColor(R.color.C00BF40))
                    tvContent.text = it.comment
                } else {
                    tvDeadline.setBackgroundResource(R.drawable.shape_4_eae4f8)
                    tvDeadline.setTextColor(requireContext().getColor(R.color.C874FFF))
                    tvContent.text = it.period
                }

                it.category?.let { category ->
                    tvCategory.text = getString(category.textId)
                    tvCategory.show()
                }
                tvMomentTitle.text = it.title
                Glide.with(ivImage)
                    .load(it.coverImage?.url)
                    .transform(CenterInside(), RoundedCorners(8))
                    .into(ivImage)

            }

            adapter.setClickListener { item ->
                when (item) {
                    Constants.MomentShareType.KAKAO -> {
                        shareKakao(it.inviteUrl)
                    }

                    Constants.MomentShareType.MESSAGE -> {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = SCHEME_SMS_TO.toUri() // 수신자 없이 메시지 앱 열기
                            putExtra(SCHEME_SMS_TO_BODY, it.inviteUrl)
                        }
                        if (intent.resolveActivity(requireContext().packageManager) != null) {
                            requireActivity().startActivity(intent)
                        }
                    }

                    Constants.MomentShareType.SHARE -> {
                        val share = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            type = SCHEME_SHARE_TYPE
                            putExtra(Intent.EXTRA_TEXT, it.inviteUrl)
                        }, getString(R.string.app_name))
                        startActivity(share)
                    }

                    Constants.MomentShareType.COPY -> {
                        val clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(getString(R.string.app_name), it.inviteUrl)
                        clipboard.setPrimaryClip(clip)
                    }
                }
            }
        }
    }

    private fun shareKakao(inviteUrl: String) {
        val map = mapOf(MOMENT_SHARE_KAKAO_WEB_KEY to inviteUrl)
        val templateId = BuildConfig.KAKAO_SHARE_TEMPLATE_ID.toLong()

        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            ShareClient.instance.shareCustom(requireContext(), templateId, templateArgs = map) { sharingResult, error ->
                if (error == null && sharingResult != null) {
                    startActivity(sharingResult.intent)
                }
            }
        } else {
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId, map)

            try {
                KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
            } catch (_: UnsupportedOperationException) {
            }
        }
    }
}