package kr.co.rolling.moment.feature.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.rolling.moment.R


/**
 * 바텀시트 화면 추상클래스 정의
 */
abstract class BaseBottomSheetFragment<B : ViewBinding> : BottomSheetDialogFragment() {
    protected lateinit var binding: B

    protected val slideUp: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let { dialogWindow ->
            dialogWindow.setGravity(Gravity.BOTTOM)
            dialogWindow.setBackgroundDrawableResource(R.color.C17171985)
            dialogWindow.setWindowAnimations(R.style.AppBottomSheetDialogAnimation)
            dialog.setCancelable(false)
        }

        dialog.setOnShowListener {
            val castDialog = it as BottomSheetDialog
            val bottomSheet = castDialog.findViewById<View?>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(android.R.color.transparent)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false // ✅ 드래그 불가 설정 추가
            behavior.skipCollapsed = true // ✅ 아래로 내리면 collapse로 안 감

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    abstract fun init()
}
