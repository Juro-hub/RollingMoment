package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemMomentCategoryBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.MomentCreateCategoryInfo
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 모먼트 카테고리 선택 Adapter
 */
class MomentCategoryAdapter : ListAdapter<MomentCreateCategoryInfo, BaseViewHolder<MomentCreateCategoryInfo>>(DiffCallback()) {
    private var itemClickListener: ((category: MomentCreateCategoryInfo) -> Unit)? = null
    private var selectedItem: String? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentCreateCategoryInfo> {
        val view = ItemMomentCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentCreateCategoryInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((category: MomentCreateCategoryInfo) -> Unit)) {
        itemClickListener = click
    }

    fun setSelectedItem(text: String) {
        selectedItem = text
    }

    inner class ViewHolder(private val binding: ItemMomentCategoryBinding) : BaseViewHolder<MomentCreateCategoryInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: MomentCreateCategoryInfo) = with(binding) {
            binding.btnCategory.text = item.title
            binding.btnCategory.setOnSingleClickListener {
                itemClickListener?.invoke(item)
            }

            if (selectedItem == item.title) {
                binding.btnCategory.setBackgroundDrawable(root.context.getDrawable(R.drawable.shape_12_ffffff_171719))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentCreateCategoryInfo>() {
        override fun areItemsTheSame(oldItem: MomentCreateCategoryInfo, newItem: MomentCreateCategoryInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentCreateCategoryInfo, newItem: MomentCreateCategoryInfo): Boolean {
            return oldItem == newItem
        }
    }
}