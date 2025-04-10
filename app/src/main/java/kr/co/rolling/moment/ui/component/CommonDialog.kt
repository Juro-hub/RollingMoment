package kr.co.rolling.moment.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.DialogCommonBinding
import kr.co.rolling.moment.library.util.getParcelableCompat

/**
 * 공통 Dialog
 */
class CommonDialog : DialogFragment(R.layout.dialog_common) {
    companion object {
        /** Dialog 표시 데이터 */
        const val ARG_DIALOG_DATA = "arg_dialog_data"
    }

    private lateinit var binding: DialogCommonBinding

    /** 긍정 버튼 Call back */
    private var positiveCallback: (() -> Unit)? = null

    /** 부정 버튼 Call back */
    private var negativeCallback: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_common, container, false)

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

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().add(this, tag).commitAllowingStateLoss()
    }

    private fun initViewBinding(view: View) {
        val data = arguments.getParcelableCompat(ARG_DIALOG_DATA, CommonDialogData::class.java)
        if (data != null) {
            dialog?.setCancelable(false)

            binding = DialogCommonBinding.bind(view)

            binding.tvTitle.text = data.title
            binding.tvContent.text = data.contents
            binding.btnPositive.text = data.positiveText
            binding.btnNegative.text = data.negativeText

            binding.btnPositive.setOnClickListener {
                positiveCallback?.invoke()
                dismiss()
            }

            binding.btnNegative.setOnClickListener {
                negativeCallback?.invoke()
                dismiss()
            }

            binding.btnNegative.isVisible = data.negativeText.isNotEmpty()
        } else {
            dismiss()
        }
    }

    /**
     * @param listener 긍정 버튼 Action 설정
     */
    fun setPositiveListener(listener: (() -> Unit)? = null) {
        positiveCallback = listener
    }

    /**
     * @param listener 부정 버튼 Action 설정
     */
    fun setNegativeListener(listener: (() -> Unit)? = null) {
        negativeCallback = listener
    }
}