package kr.co.rolling.moment.feature.moment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kr.co.rolling.moment.databinding.ItemMomentCoverTypeBinding
import kr.co.rolling.moment.feature.base.BaseViewHolder
import kr.co.rolling.moment.library.data.MomentCoverType
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import java.io.ByteArrayOutputStream


/**
 * Moment Cover 관련 Adapter
 */
class MomentCreateCoverAdapter : ListAdapter<MomentCoverType, BaseViewHolder<MomentCoverType>>(DiffCallback()) {
    private var itemClickListener: ((uri: Uri) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentCoverType> {
        val view = ItemMomentCoverTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MomentCoverType>, position: Int) {
        holder.bind(getItem(position))
    }

    fun setClickListener(click: ((uri: Uri) -> Unit)) {
        itemClickListener = click
    }

    inner class ViewHolder(private val binding: ItemMomentCoverTypeBinding) : BaseViewHolder<MomentCoverType>(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(item: MomentCoverType) = with(binding) {
            Glide.with(itemView.context)
                .load(item.resID)
                .fitCenter()
                .into(ivType)

            ivType.setOnSingleClickListener {
                //TODO 호출 URL 확인 후 변경 필요
                itemClickListener?.invoke(getImageUri(root.context, ivType.drawable.toBitmap()))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MomentCoverType>() {
        override fun areItemsTheSame(oldItem: MomentCoverType, newItem: MomentCoverType): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: MomentCoverType, newItem: MomentCoverType): Boolean {
            return oldItem == newItem
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}