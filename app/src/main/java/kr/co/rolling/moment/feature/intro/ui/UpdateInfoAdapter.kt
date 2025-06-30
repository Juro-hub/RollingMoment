package kr.co.rolling.moment.feature.intro.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemMomentCategoryBinding
import kr.co.rolling.moment.databinding.ItemUpdateInfoBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 업데이트 목록 Adapter
 */
class UpdateInfoAdapter : ListAdapter<String, BaseViewHolder<String>>(DiffCallback()) {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val view = ItemUpdateInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemUpdateInfoBinding) : BaseViewHolder<String>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: String) = with(binding) {
            tvUpdateInfo.text = item
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