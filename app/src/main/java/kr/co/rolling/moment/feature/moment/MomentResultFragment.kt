package kr.co.rolling.moment.feature.moment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentResultBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.network.data.response.MomentEnrollInfo
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

        adapter.setClickListener { item ->
            when (item) {
                Constants.MomentShareType.KAKAO -> {

                }

                Constants.MomentShareType.MESSAGE -> {

                }

                Constants.MomentShareType.SHARE -> {

                }

                Constants.MomentShareType.COPY -> {

                }
            }
        }
    }

    override fun observeViewModel() {
        viewModel.requestMomentEnroll(args.momentCode)

        viewLifecycleOwner.observeEvent(viewModel.momentEnroll, ::handleMomentShareResult)
    }

    private fun handleMomentShareResult(event: SingleEvent<MomentEnrollInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentEnroll: data = ${it}")

            binding.layoutMoment.apply {
                ivMore.hide()
                tvDeadline.text = it.deadline
                if (it.isExpired) {
                    tvDeadline.setBackgroundResource(R.drawable.shape_4_e7f5e7)
                    tvDeadline.setTextColor(requireContext().getColor(R.color.C00BF40))
                } else {
                    tvDeadline.setBackgroundResource(R.drawable.shape_4_eae4f8)
                    tvDeadline.setTextColor(requireContext().getColor(R.color.C874FFF))
                }

                tvCategory.text = getString(it.category.textId)
                tvMomentTitle.text = it.title
                tvContent.text = it.comment
                Glide.with(ivImage)
                    .load(it.coverImageUrl)
                    .transform(CenterInside(), RoundedCorners(8))
                    .into(ivImage)

            }
        }
    }
}