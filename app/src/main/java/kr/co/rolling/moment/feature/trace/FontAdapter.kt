package kr.co.rolling.moment.feature.trace

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.databinding.ItemFontBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.TraceFontType
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 흔적 작성 관련 텍스트 정렬 Adapter
 */
class FontAdapter : ListAdapter<TraceFontType, BaseViewHolder<TraceFontType>>(DiffCallback()) {
    private var itemClickListener: ((type: TraceFontType) -> Unit)? = null
    private var selectedItem: TraceFontType = TraceFontType.DEFAULT

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TraceFontType> {
        val view = ItemFontBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TraceFontType>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((type: TraceFontType) -> Unit)) {
        itemClickListener = click
    }

    fun setSelectedItem(selectedItem: TraceFontType) {
        this.selectedItem = selectedItem
    }

    inner class ViewHolder(private val binding: ItemFontBinding) : BaseViewHolder<TraceFontType>(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: TraceFontType) {
            binding.tvType.text = item.title
            binding.ivCheck.isVisible = selectedItem == item

            binding.tvType.typeface = ResourcesCompat.getFont(binding.root.context, item.fontRes)

            binding.root.setOnSingleClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TraceFontType>() {
        override fun areItemsTheSame(oldItem: TraceFontType, newItem: TraceFontType): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: TraceFontType, newItem: TraceFontType): Boolean {
            return oldItem.name == newItem.name
        }
    }
}