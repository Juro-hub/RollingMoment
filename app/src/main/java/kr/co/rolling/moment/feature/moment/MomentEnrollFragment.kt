package kr.co.rolling.moment.feature.moment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.FragmentMomentEnrollBinding
import kr.co.rolling.moment.feature.base.BaseFragment
import kr.co.rolling.moment.library.data.Constants
import kr.co.rolling.moment.library.network.data.response.MomentEnrollInfo
import kr.co.rolling.moment.library.network.util.SingleEvent
import kr.co.rolling.moment.library.network.viewmodel.MomentViewModel
import kr.co.rolling.moment.library.util.navigateSafe
import kr.co.rolling.moment.library.util.observeEvent
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show
import timber.log.Timber

/**
 * 탐색을 통하여 모먼트 진입 시 노출되는 화면
 */
@AndroidEntryPoint
class MomentEnrollFragment : BaseFragment(R.layout.fragment_moment_enroll) {
    private lateinit var binding: FragmentMomentEnrollBinding
    private val args by navArgs<MomentEnrollFragmentArgs>()
    private val viewModel by activityViewModels<MomentViewModel>()

    override fun initViewBinding(view: View) {
        binding = FragmentMomentEnrollBinding.bind(view)

        val randomColor = Constants.TraceBackgroundColor.entries.random().color
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), randomColor))
        binding.layoutToolBar.ivBack.setOnSingleClickListener {
            finishFragment()
        }

        binding.btnEnroll.setOnSingleClickListener {
            findNavController().navigateSafe(MomentEnrollFragmentDirections.actionMomentEnrollFragmentToMomentDetailFragment(args.momentCode))
        }
    }

    override fun observeViewModel() {
        viewModel.requestMomentEnroll(args.momentCode)

        viewLifecycleOwner.observeEvent(viewModel.momentEnroll, ::handleMomentEnroll)
    }

    private fun handleMomentEnroll(event: SingleEvent<MomentEnrollInfo>) {
        event.getContentIfNotHandled()?.let {
            Timber.d("handleMomentEnroll: data = ${it}")

            binding.tvMomentTitle.text = getString(R.string.moment_detail_enroll, it.title)
            binding.layoutMoment.apply {
                tvDeadline.text = it.deadline
                if (it.isExpired) {
                    tvDeadline.setBackgroundResource(R.drawable.shape_4_e7f5e7)
                    tvDeadline.setTextColor(root.context.getColor(R.color.C00BF40))
                } else {
                    tvDeadline.setBackgroundResource(R.drawable.shape_4_eae4f8)
                    tvDeadline.setTextColor(root.context.getColor(R.color.C874FFF))
                }

                it.category?.let { category ->
                    tvCategory.text = getString(category.textId)
                    tvCategory.show()
                }
                tvMomentTitle.text = it.title
                tvContent.text = it.comment
                Glide.with(ivImage)
                    .load(it.coverImageUrl)
                    .transform(CenterInside(), RoundedCorners(root.resources.getDimensionPixelSize(R.dimen.spacing_8)))
                    .into(ivImage)
                ivMore.hide()
            }
        }
    }
}