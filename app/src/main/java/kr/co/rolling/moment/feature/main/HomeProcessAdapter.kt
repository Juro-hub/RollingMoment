package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemProcessTypeBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.ui.util.hide
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show

/**
 * 홈 탭 내 모먼트 목록 어댑터
 */
class HomeProcessAdapter : ListAdapter<String, BaseViewHolder<String>>(DiffCallback()) {
    private var itemClickListener: ((title: String) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val view = ItemProcessTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((title: String) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemProcessTypeBinding) : BaseViewHolder<String>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: String) = with(binding) {
            if ((absoluteAdapterPosition == selectedPosition) || (absoluteAdapterPosition == 0 && selectedPosition == RecyclerView.NO_POSITION)) {
                binding.tvProcess.setTextColor(binding.root.context.getColor(R.color.C171719))
                binding.viewUnderLine.show()
            } else {
                binding.tvProcess.setTextColor(binding.root.context.getColor(R.color.CB2B2B2))
                binding.viewUnderLine.hide()
            }

            binding.tvProcess.text = item

            binding.root.setOnSingleClickListener {
                selectedPosition = absoluteAdapterPosition
                notifyDataSetChanged()

                itemClickListener?.invoke(binding.tvProcess.text.toString())
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