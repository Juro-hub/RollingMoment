package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.databinding.ItemNotificationBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.NotificationInfo
import kr.co.rolling.moment.library.util.htmlToSpanned
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 홈 탭 내 모먼트 목록 어댑터
 */
class NotificationAdapter : ListAdapter<NotificationInfo, BaseViewHolder<NotificationInfo>>(DiffCallback()) {
    private var itemClickListener: ((item: NotificationInfo) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NotificationInfo> {
        val view = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<NotificationInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: NotificationInfo) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) : BaseViewHolder<NotificationInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: NotificationInfo) = with(binding) {
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

    class DiffCallback : DiffUtil.ItemCallback<NotificationInfo>() {
        override fun areItemsTheSame(oldItem: NotificationInfo, newItem: NotificationInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: NotificationInfo, newItem: NotificationInfo): Boolean {
            return oldItem == newItem
        }
    }
}