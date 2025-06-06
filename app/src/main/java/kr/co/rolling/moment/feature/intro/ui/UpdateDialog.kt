package kr.co.rolling.moment.feature.intro.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.DialogUpdateBinding
import kr.co.rolling.moment.library.network.data.response.SplashInfo
import kr.co.rolling.moment.library.util.getParcelableCompat
import kr.co.rolling.moment.library.util.moveToMarket
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show

class UpdateDialog : DialogFragment(R.layout.dialog_update) {
    companion object {
        /** Dialog 표시 데이터 */
        const val ARG_DIALOG_DATA = "arg_dialog_data"
    }

    private lateinit var binding: DialogUpdateBinding

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
        val data = arguments.getParcelableCompat(ARG_DIALOG_DATA, SplashInfo::class.java)
        if (data != null) {
            dialog?.setCancelable(false)

            binding = DialogUpdateBinding.bind(view)
            binding.btnPositive.setOnSingleClickListener {
                requireContext().moveToMarket()
            }

            if(data.updateMessage.isNotEmpty()){
                binding.layoutUpdateInfo.show()

                //TODO
            }

        } else {
            dismiss()
        }
    }
}