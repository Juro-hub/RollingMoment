package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemTextAlignBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.Constants.TraceTextAlign
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 작성 관련 텍스트 정렬 Adapter
 */
class TraceAlignmentAdapter : ListAdapter<TraceTextAlign, BaseViewHolder<TraceTextAlign>>(DiffCallback()) {
    private var itemClickListener: ((type: TraceTextAlign) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TraceTextAlign> {
        val view = ItemTextAlignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TraceTextAlign>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((type: TraceTextAlign) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemTextAlignBinding) : BaseViewHolder<TraceTextAlign>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: TraceTextAlign) {
            binding.tvAlign.text = binding.root.context.getString(item.string)
            binding.ivAlign.setImageDrawable(ContextCompat.getDrawable(binding.root.context, item.drawable))

            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 6f
                setColor(binding.root.context.getColor(R.color.CFAFAFA))
            }

            if (isSelectedItem(bindingAdapterPosition, selectedPosition, absoluteAdapterPosition)) {
                TextViewCompat.setTextAppearance(binding.tvAlign, R.style.L512Bold)
                binding.tvAlign.setTextColor(binding.root.context.getColor(R.color.C171719))

                // 선택된 아이템의 경우 Stroke 설정
                drawable.setStroke(2, binding.root.context.getColor(R.color.C171719))
            } else {
                TextViewCompat.setTextAppearance(binding.tvAlign, R.style.L612Medium)
                binding.tvAlign.setTextColor(binding.root.context.getColor(R.color.C7F7F7F))
            }

            binding.ivAlign.background = drawable
            binding.root.setOnSingleClickListener {
                selectedPosition = bindingAdapterPosition

                notifyDataSetChanged()

                itemClickListener?.invoke(item)
            }
        }
    }

    private fun isSelectedItem(adapterPosition: Int, selectedPosition : Int, absolutePosition: Int) : Boolean{
        return (adapterPosition == selectedPosition) || (absolutePosition == 0 && selectedPosition == RecyclerView.NO_POSITION)
    }

    class DiffCallback : DiffUtil.ItemCallback<TraceTextAlign>() {
        override fun areItemsTheSame(oldItem: TraceTextAlign, newItem: TraceTextAlign): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: TraceTextAlign, newItem: TraceTextAlign): Boolean {
            return oldItem.name == newItem.name
        }
    }
}