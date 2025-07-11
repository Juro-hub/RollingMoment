package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.databinding.ItemMomentExpiredBinding
import kr.co.rolling.moment.databinding.ItemRecentSearchBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.ui.util.setOnSingleClickListener


/**
 * 검색 내 최근 목록
 */
class RecentSearchAdapter : ListAdapter<String, BaseViewHolder<String>>(DiffCallback()) {
    private var rootClickListener: ((item: String) -> Unit)? = null
    private var deleteClickListener: ((item: String) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val view = ItemRecentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: String) -> Unit)) {
        rootClickListener = click
    }

    fun setDelete(click: ((item: String) -> Unit)) {
        deleteClickListener = click
    }

    inner class ViewHolder(private val binding: ItemRecentSearchBinding) : BaseViewHolder<String>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: String) = with(binding) {
            binding.tvRecent.text = item

            root.setOnSingleClickListener {
                rootClickListener?.invoke(item)
            }

            ivDelete.setOnSingleClickListener {
                deleteClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}