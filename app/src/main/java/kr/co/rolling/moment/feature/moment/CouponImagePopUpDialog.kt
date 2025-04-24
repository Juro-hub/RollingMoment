/*
 * Create by msJeong on 2023. 11. 14 Copyright (c) 2023. msJeong. All rights reserved. 
 */

package kr.co.rolling.moment.feature.moment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.DialogFragmentCouponImageBinding
import kr.co.rolling.moment.ui.util.setOnSingleClickListener

/**
 * 쿠폰 이미지 팝업 Dialog
 */
@AndroidEntryPoint
class CouponImagePopUpDialog : DialogFragment(R.layout.dialog_fragment_coupon_image) {
    /** 화면에 따른 View Binding */
    private lateinit var binding: DialogFragmentCouponImageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_fragment_coupon_image, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 화면 Init
     * @param view 현재 노출되는 View
     */
    private fun initViewBinding(view: View) {
        binding = DialogFragmentCouponImageBinding.bind(view)

        binding.ivCouponImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.img))
        binding.ivCouponImgClose.setOnSingleClickListener() {
            dismiss()
        }

    }
}