package kr.co.rolling.moment.feature.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * 기본 어뎁터 Binding 처리 구성 추상 클래스
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Template Item 정보 binding 처리 호출
     */
    abstract fun bind(item: T)
}