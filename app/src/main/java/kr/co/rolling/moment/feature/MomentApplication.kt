package kr.co.rolling.moment.feature

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import kr.co.rolling.moment.BuildConfig
import kr.co.rolling.moment.R
import kr.co.rolling.moment.databinding.ActivityLoadingPopupBinding
import timber.log.Timber
import javax.inject.Inject

/**
 * Application Class
 */
@HiltAndroidApp
class MomentApplication @Inject constructor() : Application() {

    private var dialog: AppCompatDialog? = null
    private lateinit var binding: ActivityLoadingPopupBinding
    private var dialogActivityName: String? = null

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
        NaverIdLoginSDK.initialize(this, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_KEY, getString(R.string.app_name))
    }

    /**
     * 로딩 Dialog 객체 초기화
     *
     * @param activity : 생성 호출 한 Activity 객체
     */
    private fun initDialog(activity: Activity) {
        dialog = AppCompatDialog(activity)
        dialogActivityName = activity.javaClass.name
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        binding = ActivityLoadingPopupBinding.inflate(LayoutInflater.from(activity), null, false)
        dialog?.setContentView(binding.root)
    }

    /**
     * 로딩 Dialog 표시
     *
     * @param activity : 표시 할 Activity 객체
     */
    fun showLoading(activity: Activity?) {
        activity?.let {
            Timber.d("showDialog() called with: activity = [${it::class.java.simpleName}]")
            if (activity.isFinishing) {
                return
            }

            if (dialog == null || dialog?.isShowing == false) {
                initDialog(it)
                dialog?.show()
                binding.lottieAnim.playAnimation()
            }
        }
    }

    /**
     * 로딩 Dialog 숨김
     *
     * @param activityName : 숨김 처리하는 화면 Class 명
     */
    fun hideLoading(activityName: String?) {
        if (dialog?.isShowing == true && (activityName == null || activityName == dialogActivityName)) {
            Timber.d("hideDialog() called")
            binding.lottieAnim.clearAnimation()
            dialog?.dismiss()
        }
    }
}