package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemTraceBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.MomentTraceInfo
import kr.co.rolling.moment.library.network.data.response.ReactionInfo
import kr.co.rolling.moment.ui.util.setOnSingleClickListener


/**
 * Moment Cover 관련 Adapter
 */
class MomentDetailTraceAdapter : ListAdapter<MomentTraceInfo, BaseViewHolder<MomentTraceInfo>>(DiffCallback()) {
    private var goodClickListener: ((item: MomentTraceInfo) -> Unit)? = null
    private var reportClickListener: ((item: MomentTraceInfo) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentTraceInfo> {
        val view = ItemTraceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentTraceInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((item: MomentTraceInfo) -> Unit)) {
        goodClickListener = click
    }

    fun setReportListener(click: ((item: MomentTraceInfo)-> Unit)){
        reportClickListener = click
    }

    inner class ViewHolder(private val binding: ItemTraceBinding) : BaseViewHolder<MomentTraceInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
        override fun bind(item: MomentTraceInfo) = with(binding) {
            val baseDrawable = if (item.color.color != null) {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 8f
                    setColor(binding.root.context.getColor(item.color.color))
                }
            } else {
                ContextCompat.getDrawable(binding.root.context, item.color.drawable ?: R.drawable.bg_heart_crop)
            }

            binding.layoutTrace.background = baseDrawable

            binding.tvContent.text = item.content
            binding.tvContent.typeface = ResourcesCompat.getFont(binding.root.context, item.font.fontRes)
            binding.tvContent.gravity = item.alignment.gravity
            binding.tvContent.setTextColor(root.context.getColor(item.textColor.color))
            binding.tvMine.isVisible = item.isOwner

            binding.tvCount.text = (item.reactions?.get(0)?.count ?: 0).toString()
            if (item.reactions?.get(0)?.isClicked == true) {
                binding.tvCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_already, 0, 0, 0)
                binding.tvCount.setTextColor(root.context.getColor(R.color.C874FFF))
            } else {
                binding.tvCount.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up, 0, 0, 0)
                binding.tvCount.setTextColor(root.context.getColor(R.color.C7F7F7F))
            }

            binding.tvCount.setOnSingleClickListener {
                goodClickListener?.invoke(item)
            }

            binding.ivReport.setOnSingleClickListener{
                reportClickListener?.invoke(item)
            }
            binding.tvInfo.text = binding.root.context.getString(R.string.moment_detail_trace_date_user, item.nickname, item.date)
        }
    }

    fun updateReaction(code: String, updatedReactions: List<ReactionInfo>?) {
        val index = currentList.indexOfFirst { it.code == code }
        if (index != -1) {
            val currentItem = currentList[index]
            val updatedItem = currentItem.copy(reactions = updatedReactions)

            val newList = currentList.toMutableList()
            newList[index] = updatedItem
            submitList(newList)
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<MomentTraceInfo>() {
        override fun areItemsTheSame(oldItem: MomentTraceInfo, newItem: MomentTraceInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentTraceInfo, newItem: MomentTraceInfo): Boolean {
            return oldItem == newItem
        }
    }
}