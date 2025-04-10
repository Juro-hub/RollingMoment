package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.LayoutMomentInfoBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.HomeMomentInfo
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.showExpandableText

/**
 * 홈 탭 내 모먼트 목록(진행중) 어댑터
 */
class HomeIngMomentAdapter : ListAdapter<HomeMomentInfo, BaseViewHolder<HomeMomentInfo>>(DiffCallback()) {
    private var rootClickListener: ((item: HomeMomentInfo) -> Unit)? = null
    private var moreClickListener: ((item: HomeMomentInfo) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeMomentInfo> {
        val view = LayoutMomentInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<HomeMomentInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: HomeMomentInfo) -> Unit)) {
        rootClickListener = click
    }

    fun setInfoClickListener(click: ((item: HomeMomentInfo) -> Unit)) {
        moreClickListener = click
    }

    inner class ViewHolder(private val binding: LayoutMomentInfoBinding) : BaseViewHolder<HomeMomentInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: HomeMomentInfo) = with(binding) {
            Glide.with(ivImage)
                .load(item.coverImageUrl)
                .transform(CenterInside(), RoundedCorners(8))
                .into(ivImage)

            ivMore.isVisible = item.isOwner
            tvDeadline.text = item.deadline
            tvCategory.text = root.context.getString(item.category.textId)
            tvMomentTitle.text = item.title
            tvContent.showExpandableText(
                item.comment
            )

            if (item.isExpired) {
                tvDeadline.setBackgroundResource(R.drawable.shape_4_e7f5e7)
                tvDeadline.setTextColor(root.context.getColor(R.color.C00BF40))
            } else {
                tvDeadline.setBackgroundResource(R.drawable.shape_4_eae4f8)
                tvDeadline.setTextColor(root.context.getColor(R.color.C874FFF))
            }
            root.setOnSingleClickListener {
                rootClickListener?.invoke(item)
            }

            ivMore.setOnSingleClickListener {
                moreClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeMomentInfo>() {
        override fun areItemsTheSame(oldItem: HomeMomentInfo, newItem: HomeMomentInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: HomeMomentInfo, newItem: HomeMomentInfo): Boolean {
            return oldItem == newItem
        }
    }
}