package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemMomentCategoryBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.MomentCategoryType
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 *
 */
class MomentCategoryAdapter : ListAdapter<MomentCategoryType, BaseViewHolder<MomentCategoryType>>(DiffCallback()) {
    private var itemClickListener: ((title: String) -> Unit)? = null
    private var selectedItem: String? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentCategoryType> {
        val view = ItemMomentCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentCategoryType>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((title: String) -> Unit)) {
        itemClickListener = click
    }

    fun setSelectedItem(text: String) {
        selectedItem = text
    }

    inner class ViewHolder(private val binding: ItemMomentCategoryBinding) : BaseViewHolder<MomentCategoryType>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: MomentCategoryType) = with(binding) {
            binding.btnCategory.text = item.title
            binding.btnCategory.setOnSingleClickListener {
                itemClickListener?.invoke(item.title)
            }

            if (selectedItem == item.title) {
                binding.btnCategory.setBackgroundDrawable(root.context.getDrawable(R.drawable.shape_12_ffffff_171719))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentCategoryType>() {
        override fun areItemsTheSame(oldItem: MomentCategoryType, newItem: MomentCategoryType): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentCategoryType, newItem: MomentCategoryType): Boolean {
            return oldItem == newItem
        }
    }
}