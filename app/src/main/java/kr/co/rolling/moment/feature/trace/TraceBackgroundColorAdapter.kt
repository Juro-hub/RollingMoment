package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemTraceBackgroundBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.Constants.TraceBackgroundColor
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 남기기 배경 색상 Adapter
 */
class TraceBackgroundColorAdapter : ListAdapter<TraceBackgroundColor, BaseViewHolder<TraceBackgroundColor>>(DiffCallback()) {
    private var itemClickListener: ((color: TraceBackgroundColor) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TraceBackgroundColor> {
        val view = ItemTraceBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TraceBackgroundColor>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((color: TraceBackgroundColor) -> Unit)) {
        itemClickListener = click
    }

    fun setItem(backgroundColor: TraceBackgroundColor) {
        val position = currentList.indexOfFirst { it == backgroundColor }
        if (position != -1) {
            selectedPosition = position
            itemClickListener?.invoke(backgroundColor)
            notifyDataSetChanged()
        }
    }
    inner class ViewHolder(private val binding: ItemTraceBackgroundBinding) : BaseViewHolder<TraceBackgroundColor>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: TraceBackgroundColor) = with(binding) {
            val baseDrawable = if (item.color != null) {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 8f
                    setColor(binding.root.context.getColor(item.color))
                }
            } else {
                ContextCompat.getDrawable(binding.root.context, item.thumbnail ?: R.drawable.bg_heart)
            }

            val isSelected = (absoluteAdapterPosition == selectedPosition) ||
                    (absoluteAdapterPosition == 0 && selectedPosition == RecyclerView.NO_POSITION)

            val drawable = createBackgroundWithOptionalStroke(
                baseDrawable = baseDrawable,
                showStroke = isSelected,
                strokeColor = binding.root.context.getColor(R.color.C171719)
            )

            binding.root.background = drawable

            binding.root.setOnSingleClickListener {
                selectedPosition = absoluteAdapterPosition

                notifyDataSetChanged()

                itemClickListener?.invoke(item)
            }
        }
    }

    private fun createBackgroundWithOptionalStroke(
        baseDrawable: Drawable?,
        showStroke: Boolean,
        strokeColor: Int = Color.TRANSPARENT,
    ): Drawable? {
        if (baseDrawable == null) return null
        val strokeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(2, strokeColor)
            this.cornerRadius = 8f
            setColor(Color.TRANSPARENT)
        }

        return if (showStroke) {
            LayerDrawable(arrayOf(baseDrawable, strokeDrawable))
        } else {
            baseDrawable
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TraceBackgroundColor>() {
        override fun areItemsTheSame(oldItem: TraceBackgroundColor, newItem: TraceBackgroundColor): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: TraceBackgroundColor, newItem: TraceBackgroundColor): Boolean {
            return oldItem == newItem
        }
    }
}