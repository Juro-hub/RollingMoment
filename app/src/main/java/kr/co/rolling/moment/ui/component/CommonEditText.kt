package kr.co.rolling.moment.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.LayoutCommonEditTextBinding
import kr.co.rolling.moment.ui.util.focusAndShowKeyboard
import kr.co.rolling.moment.ui.util.hideKeyboard
import kr.co.rolling.moment.ui.util.setOnSingleClickListener
import kr.co.rolling.moment.ui.util.show

/**
 * 공통 입력 EditText
 */
class CommonEditText : ConstraintLayout, OnFocusChangeListener {
    /**
     * IME Option 설정
     */
    private enum class ImeOptions(val value: Int) {
        /** 완료 */
        ACTION_DONE(0),

        /** 다음 */
        ACTION_NEXT(1)
    }

    private var maxLength = 0
    private lateinit var binding: LayoutCommonEditTextBinding

    /** Text Change Listener */
    private var textChangeListener: (data: String) -> Unit = {}

    override fun clearFocus() {
        binding.etData.clearFocus()
        super.clearFocus()
    }

    constructor(context: Context) : super(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        init()
        initLayoutSetting(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        initLayoutSetting(context, attrs)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            requestFocused()
        } else {
            view?.hideKeyboard()
        }

        val backgroundColor = if (hasFocus) {
            R.drawable.shape_8_171719_ffffff
        } else {
            R.drawable.shape_8_e8e8ea_ffffff
        }

        binding.etData.setBackgroundResource(backgroundColor)
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LayoutCommonEditTextBinding.inflate(inflater, this, true)
        binding.etData.id = generateViewId()

        binding.root.setOnSingleClickListener {
            binding.etData.focusAndShowKeyboard()
        }

        binding.etData.text.clear()
        binding.etData.addTextChangedListener {
            it?.let { editable ->
                binding.tvMaxLength.text = context.getString(R.string.edit_text_length_check, editable.length, maxLength)

                changeUiVisible()

                textChangeListener.invoke(editable.toString())
            }
        }

        binding.ivCancel.setOnSingleClickListener {
            binding.etData.setText("")
            showErrorCase(false)
        }
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun initLayoutSetting(context: Context, attrs: AttributeSet) {
        val attribute = context.obtainStyledAttributes(attrs, R.styleable.CommonEditTextAttributes)

        binding.tvHelper.text = attribute.getString(R.styleable.CommonEditTextAttributes_helperText)
        binding.tvHint.text = attribute.getString(R.styleable.CommonEditTextAttributes_hintText)
        binding.tvError.text = attribute.getString(R.styleable.CommonEditTextAttributes_errorMessage)

        maxLength = attribute.getInt(R.styleable.CommonEditTextAttributes_inputMaxLength, -1)
        if (maxLength > 0) {
            binding.etData.filters += arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }

        binding.tvMaxLength.isVisible = attribute.getBoolean(R.styleable.CommonEditTextAttributes_isVisibleMaxLength, false)
        binding.tvMaxLength.text = context.getString(R.string.edit_text_length_check, 0, maxLength)

        val guideText = attribute.getString(R.styleable.CommonEditTextAttributes_guideText)
        binding.tvGuide.text = guideText
        if (guideText?.isNotEmpty() == true) {
            binding.tvGuide.show()
        }

        // imeOption 설정
        binding.etData.imeOptions = when (ImeOptions.entries[attribute.getInt(R.styleable.CommonEditTextAttributes_inputImeOptions, ImeOptions.ACTION_NEXT.value)]) {
            ImeOptions.ACTION_DONE -> {
                EditorInfo.IME_ACTION_DONE
            }

            ImeOptions.ACTION_NEXT -> {
                EditorInfo.IME_ACTION_NEXT
            }
        }

        binding.etData.onFocusChangeListener = this
    }

    private fun changeUiVisible() {
        binding.tvHint.isVisible = binding.etData.text.isEmpty()
        binding.ivCancel.isVisible = (binding.etData.text.isNotEmpty() && binding.etData.hasFocus())
    }

    /**
     * View 에 대해 Focus 처리한다.
     */
    private fun requestFocused() {
        binding.etData.setBackgroundResource(R.drawable.shape_8_171719_ffffff)
        changeUiVisible()
    }

    fun getData() = binding.etData.text

    /**
     * @param listener EditText Text Change Listener
     */
    fun setTextChangeListener(listener: (data: String) -> Unit) {
        textChangeListener = listener
    }

    fun showErrorCase(isError: Boolean) {
        binding.tvError.isVisible = isError
    }
}