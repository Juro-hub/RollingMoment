package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.databinding.ItemTraceEmojiBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 남기기 이모지 Adapter
 */
class TraceEmojiAdapter :
    ListAdapter<String, BaseViewHolder<String>>(DiffCallback()) {
    private var itemClickListener: ((emoji: String) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseViewHolder<String> {
        val view =
            ItemTraceEmojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((color: String) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemTraceEmojiBinding) :
        BaseViewHolder<String>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: String) = with(binding) {
            binding.tvEmoji.text = item
            binding.root.setOnSingleClickListener {
                itemClickListener?.invoke(item)
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