package kr.co.rolling.moment.feature.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemCategoryFilterBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.Constants.MomentCategory
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 홈 탭 내 모먼트 목록(진행중) 어댑터
 */
class FilterAdapter : ListAdapter<MomentCategory, BaseViewHolder<MomentCategory>>(DiffCallback()) {
    private var rootClickListener: ((item: MomentCategory) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentCategory> {
        val view = ItemCategoryFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentCategory>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: MomentCategory) -> Unit)) {
        rootClickListener = click
    }

    inner class ViewHolder(private val binding: ItemCategoryFilterBinding) : BaseViewHolder<MomentCategory>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
        override fun bind(item: MomentCategory) = with(binding) {
            if ((absoluteAdapterPosition == selectedPosition) || (absoluteAdapterPosition == 0 && selectedPosition == RecyclerView.NO_POSITION)) {
                tvCategory.setBackgroundResource(R.drawable.shape_20_171719)
                tvCategory.setTextColor(root.context.getColor(R.color.CFFFFFF))
            } else {
                tvCategory.setBackgroundResource(R.drawable.shape_20_ffffff_e8e8ea)
                tvCategory.setTextColor(root.context.getColor(R.color.C4C4C4C))
            }

            tvCategory.text = root.context.getString(item.textId)

            root.setOnSingleClickListener {
                selectedPosition = absoluteAdapterPosition
                notifyDataSetChanged()

                rootClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentCategory>() {
        override fun areItemsTheSame(oldItem: MomentCategory, newItem: MomentCategory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentCategory, newItem: MomentCategory): Boolean {
            return oldItem.code == newItem.code
        }
    }
}