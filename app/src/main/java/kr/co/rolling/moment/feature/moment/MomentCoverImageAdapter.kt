package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemMomentCoverBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.ui.util.BorderTransformation
import kr.co.rolling.moment.ui.util.setOnSingleClickListener


/**
 * Moment Cover Image 관련 Adapter
 */
class MomentCoverImageAdapter : ListAdapter<String, BaseViewHolder<String>>(DiffCallback()) {
    private var itemClickListener: ((uri: String) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val view = ItemMomentCoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((uri: String) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemMomentCoverBinding) : BaseViewHolder<String>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: String) = with(binding) {
            Glide.with(ivType)
                .load(item)
                .transform(
                    MultiTransformation(
                        FitCenter(),
                        BorderTransformation(1f, root.context.getColor(R.color.CE0E0E2), root.resources.getDimensionPixelSize(R.dimen.spacing_8).toFloat()),
                        RoundedCorners(root.resources.getDimensionPixelSize(R.dimen.spacing_8))
                    )
                )
                .into(ivType)

            ivType.setOnSingleClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}