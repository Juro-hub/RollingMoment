package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemTraceBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.data.response.TraceInfo


/**
 * Moment Cover 관련 Adapter
 */
class MomentDetailTraceAdapter : ListAdapter<TraceInfo, BaseViewHolder<TraceInfo>>(DiffCallback()) {
    private var goodClickListener: ((code: String) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TraceInfo> {
        val view = ItemTraceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TraceInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((code: String) -> Unit)) {
        goodClickListener = click
    }

    inner class ViewHolder(private val binding: ItemTraceBinding) : BaseViewHolder<TraceInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
        override fun bind(item: TraceInfo) = with(binding) {
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = root.context.resources.getDimensionPixelSize(R.dimen.spacing_8).toFloat()
                setColor(root.context.getColor(item.color.color))
            }
            binding.layoutTrace.background = drawable

            binding.tvContent.text = item.content
            binding.tvContent.typeface = ResourcesCompat.getFont(binding.root.context, item.font.fontRes)
            binding.tvContent.gravity = item.alignment.gravity

            binding.tvCount.text = (item.reactionList?.get(0)?.count ?: 0).toString()
            val image = if (item.reactionList?.get(0)?.isClick == true) {
                R.drawable.ic_thumb_up_already
            } else {
                R.drawable.ic_thumb_up
            }

            binding.tvCount.setCompoundDrawablesRelativeWithIntrinsicBounds(image, 0, 0, 0)
            binding.tvInfo.text = binding.root.context.getString(R.string.moment_detail_trace_date_user, item.nickname, item.date)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TraceInfo>() {
        override fun areItemsTheSame(oldItem: TraceInfo, newItem: TraceInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: TraceInfo, newItem: TraceInfo): Boolean {
            return oldItem == newItem
        }
    }
}