package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemMomentExpiredBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.MomentInfo
import kr.co.rolling.moment.ui.util.BorderTransformation
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show


/**
 * 홈 탭 내 모먼트 목록(만료) 어댑터
 */
class HomeExpiredAdapter : ListAdapter<MomentInfo, BaseViewHolder<MomentInfo>>(DiffCallback()) {
    private var rootClickListener: ((item: MomentInfo) -> Unit)? = null
    private var moreClickListener: ((item: MomentInfo) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentInfo> {
        val view = ItemMomentExpiredBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val binding: ItemMomentExpiredBinding) : BaseViewHolder<MomentInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: MomentInfo) = with(binding) {
            Glide.with(ivImage)
                .load(item.coverImgUrl)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        BorderTransformation(1f, root.context.getColor(R.color.CE0E0E2), root.resources.getDimensionPixelSize(R.dimen.spacing_8).toFloat()),
                        RoundedCorners(root.resources.getDimensionPixelSize(R.dimen.spacing_8))
                    )
                )
                .into(ivImage)

            tvEndDate.text = item.deadline
            item.category?.let { category ->
                tvCategory.text = root.context.getString(category.textId)
                tvCategory.show()
            }

            tvTitle.text = item.title
            tvContent.text = item.comment
            if (item.comment.isEmpty()) {
                tvContent.hide()
            }
            tvInfo.text = item.traceCnt
            ivMore.isVisible = item.isOwner
            tvPeriod.isVisible = !item.isPublic

            if (item.isExpired) {
                tvEndDate.setBackgroundResource(R.drawable.shape_4_e7f5e7)
                tvEndDate.setTextColor(root.context.getColor(R.color.C00BF40))
            } else {
                tvEndDate.setBackgroundResource(R.drawable.shape_4_eae4f8)
                tvEndDate.setTextColor(root.context.getColor(R.color.C874FFF))
            }

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