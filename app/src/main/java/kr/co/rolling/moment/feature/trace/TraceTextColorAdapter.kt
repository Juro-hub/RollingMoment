package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemTraceBackgroundBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.Constants.TraceTextAlign
import kr.co.rolling.moment.library.data.Constants.TraceTextColor
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 남기기 글자 색상 Adapter
 */
class TraceTextColorAdapter :
    ListAdapter<TraceTextColor, BaseViewHolder<TraceTextColor>>(DiffCallback()) {
    private var itemClickListener: ((color: TraceTextColor) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseViewHolder<TraceTextColor> {
        val view =
            ItemTraceBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TraceTextColor>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((color: TraceTextColor) -> Unit)) {
        itemClickListener = click
    }

    fun setItem(textColor: TraceTextColor) {
        val position = currentList.indexOfFirst { it == textColor }
        if (position != -1) {
            selectedPosition = position
            itemClickListener?.invoke(textColor)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(private val binding: ItemTraceBackgroundBinding) :
        BaseViewHolder<TraceTextColor>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: TraceTextColor) = with(binding) {
            val drawable = if ((absoluteAdapterPosition == selectedPosition) || (absoluteAdapterPosition == 0 && selectedPosition == RecyclerView.NO_POSITION)) {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setStroke(2, binding.root.context.getColor(R.color.C171719))
                    cornerRadius = 8f
                    setColor(binding.root.context.getColor(item.color))
                }
            } else {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 8f // 원하는 radius
                    setColor(root.context.getColor(item.color)) // 아이템 색상
                }
            }

            binding.root.background = drawable

            binding.root.setOnSingleClickListener {
                selectedPosition = absoluteAdapterPosition

                notifyDataSetChanged()

                itemClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TraceTextColor>() {
        override fun areItemsTheSame(oldItem: TraceTextColor, newItem: TraceTextColor): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: TraceTextColor, newItem: TraceTextColor): Boolean {
            return oldItem == newItem
        }
    }
}