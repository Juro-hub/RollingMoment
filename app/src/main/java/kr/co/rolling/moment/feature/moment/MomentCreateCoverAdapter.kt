package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ItemMomentCoverTypeBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.network.NetworkConstants
import kr.co.rolling.moment.library.network.data.response.MomentCreateCoverImageInfo
import kr.co.rolling.moment.library.network.data.response.MomentImageInfo
import kr.co.rolling.moment.library.util.CommonGridItemDecorator


/**
 * Moment Cover 관련 Adapter
 */
class MomentCreateCoverAdapter : ListAdapter<MomentCreateCoverImageInfo, BaseViewHolder<MomentCreateCoverImageInfo>>(DiffCallback()) {
    private var itemClickListener: ((image: MomentImageInfo?) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentCreateCoverImageInfo> {
        val view = ItemMomentCoverTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentCreateCoverImageInfo>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((image: MomentImageInfo?) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemMomentCoverTypeBinding) : BaseViewHolder<MomentCreateCoverImageInfo>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: MomentCreateCoverImageInfo) = with(binding) {
            val adapter = MomentCoverImageAdapter()
            adapter.setClickListener { url ->
                val clickItem = item.imageInfo.find { it.url == url }
                itemClickListener?.invoke(clickItem)
            }

            binding.tvType.text = root.context.getString(NetworkConstants.MomentCoverCategory.getCategory(item.type).textId)

            val gridLayoutManager = GridLayoutManager(root.context, 2, LinearLayoutManager.VERTICAL, false)
            adapter.submitList(item.imageInfo.map { it.url })
            rvAdapter.layoutManager = gridLayoutManager
            rvAdapter.adapter = adapter
            rvAdapter.addItemDecoration(CommonGridItemDecorator(verticalMargin = root.context.resources.getDimensionPixelSize(
                R.dimen.spacing_8), horizontalMargin = root.context.resources.getDimensionPixelSize(
                R.dimen.spacing_8), spanCount = 2))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentCreateCoverImageInfo>() {
        override fun areItemsTheSame(oldItem: MomentCreateCoverImageInfo, newItem: MomentCreateCoverImageInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentCreateCoverImageInfo, newItem: MomentCreateCoverImageInfo): Boolean {
            return oldItem == newItem
        }
    }
}