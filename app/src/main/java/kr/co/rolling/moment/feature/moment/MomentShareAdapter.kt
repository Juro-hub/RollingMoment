package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kr.co.rolling.moment.databinding.ItemShareMomentBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.Constants.MomentShareType
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 모먼트 카테고리 선택 Adapter
 */
class MomentShareAdapter : ListAdapter<MomentShareType, BaseViewHolder<MomentShareType>>(DiffCallback()) {
    private var itemClickListener: ((type: MomentShareType) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentShareType> {
        val view = ItemShareMomentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentShareType>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((category: MomentShareType) -> Unit)) {
        itemClickListener = click
    }


    inner class ViewHolder(private val binding: ItemShareMomentBinding) : BaseViewHolder<MomentShareType>(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: MomentShareType) = with(binding) {
            Glide.with(itemView.context)
                .load(item.drawableRes)
                .fitCenter()
                .into(ivIcon)

            tvIcon.setText(item.stringRes)

            root.setOnSingleClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentShareType>() {
        override fun areItemsTheSame(oldItem: MomentShareType, newItem: MomentShareType): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentShareType, newItem: MomentShareType): Boolean {
            return oldItem == newItem
        }
    }
}