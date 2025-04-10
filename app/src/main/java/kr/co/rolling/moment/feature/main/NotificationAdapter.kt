package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.databinding.ItemNotificationBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.PushItem
import kr.co.rolling.moment.library.util.htmlToSpanned
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 홈 탭 내 모먼트 목록 어댑터
 */
class NotificationAdapter : ListAdapter<PushItem, BaseViewHolder<PushItem>>(DiffCallback()) {
    private var itemClickListener: ((item: PushItem) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PushItem> {
        val view = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<PushItem>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: PushItem) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) : BaseViewHolder<PushItem>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: PushItem) = with(binding) {
            tvCategory.text = item.type
            tvTitle.text = item.content.htmlToSpanned()
            tvDate.text = item.date
            root.setOnSingleClickListener {
                itemClickListener?.invoke(item)
            }

            if (absoluteAdapterPosition == currentList.size - 1) {
                viewDivider.hide()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PushItem>() {
        override fun areItemsTheSame(oldItem: PushItem, newItem: PushItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: PushItem, newItem: PushItem): Boolean {
            return oldItem == newItem
        }
    }
}