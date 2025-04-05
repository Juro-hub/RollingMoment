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
import kr.co.rolling.moment.databinding.LayoutMomentInfoBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.showExpandableText

/**
 * 홈 탭 내 모먼트 목록(진행중) 어댑터
 */
class HomeIngMomentAdapter : ListAdapter<MomentInfo, BaseViewHolder<MomentInfo>>(DiffCallback()) {
    private var rootClickListener: ((item: MomentInfo) -> Unit)? = null
    private var moreClickListener: ((item: MomentInfo) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentInfo> {
        val view = LayoutMomentInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: MomentInfo) -> Unit)) {
        rootClickListener = click
    }

    fun setInfoClickListener(click: ((item: MomentInfo) -> Unit)) {
        moreClickListener = click
    }

    inner class ViewHolder(private val binding: LayoutMomentInfoBinding) : BaseViewHolder<MomentInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: MomentInfo) = with(binding) {
            Glide.with(ivImage)
                .load(item.coverImage)
                .transform(CenterInside(), RoundedCorners(8))
                .into(ivImage)

            ivMore.isVisible = item.isMine
            tvDeadline.text = item.deadLine
            tvCategory.text = item.category
            tvMomentTitle.text = item.title
            tvContent.showExpandableText(
                item.content
            )

            root.setOnSingleClickListener {
                rootClickListener?.invoke(item)
            }

            ivMore.setOnSingleClickListener {
                moreClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentInfo>() {
        override fun areItemsTheSame(oldItem: MomentInfo, newItem: MomentInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentInfo, newItem: MomentInfo): Boolean {
            return oldItem == newItem
        }
    }
}