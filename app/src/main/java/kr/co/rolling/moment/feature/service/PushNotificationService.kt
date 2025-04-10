package kr.co.rolling.moment.feature.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.library.util.PreferenceManager
import timber.log.Timber
import java.util.Random
import javax.inject.Inject

/**
 * Push Service
 */
@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onNewToken(token: String) {
        preferenceManager.setPushToken(token)
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("onMessageReceived : $message")

        message.notification?.let { notification ->
            Timber.d("Notification Message Title: ${notification.title}, Message Body: ${notification.body}")
            var title = notification.title
            if (title.isNullOrEmpty()) {
                title = getString(R.string.app_name)
            }
            val body = notification.body
            sendNotification(title, body /*createIntent(remoteMessage.data)*/)
            sendNotification(title, body)
        }
    }

    /**
     * 노티피케이션 설정
     */
    private fun sendNotification(title: String?, body: String?) {
        val pendingIntent = PendingIntent.getActivity(this, getNotifyId(), Intent(), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val manager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = this.getString(R.string.app_push_name)

        val channelName = this.getString(R.string.app_push_channel_name)
        val channel = NotificationChannel(notificationId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.setSound(null, null)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, notificationId)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        manager.notify(getNotifyTag(), getNotifyId(), builder.build())
    }

    /**
     * 이동 할 Intent 정보 생성
     *
     * IntroActivity로 Intent 정보 받을 수 있도록 설정
     */
//    private fun createIntent(map: Map<String, String>?): Intent {
//        return Intent(this, MomentActivity::class.java).apply {
//            map?.let {
//                val pushTitle = map[PushConstant.FCM_DATA_KEY_PUSH_TITLE]
//
//                val landingType = map[PushConstant.KEY_LANDING_TYPE]
//                val landingTarget = map[PushConstant.KEY_LANDING_TARGET]
//                val landingTargetUrl = map[PushConstant.KEY_LANDING_TARGET_URL]
//                val customData = map[PushConstant.KEY_LANDING_CUSTOM_DATA]
//
//
//                Timber.d("createIntent() called landingType = [$landingType], landingTarget = [$landingTarget], landingTargetUrl = [$landingTargetUrl], customData = [$customData]")
//
//                val bundle = Bundle()
//                bundle.putString(PushConstant.FCM_DATA_KEY_PUSH_TITLE, pushTitle)
//
//                when (checkLandingStandard(map)) {
//                    PushConstant.LandingStandard.NEW_TYPE -> {
//                        Timber.d("landingService NEW_TYPE")
//
//                        bundle.putString(PushConstant.KEY_LANDING_TYPE, landingType)
//                        bundle.putString(PushConstant.KEY_LANDING_TARGET, landingTarget)
//                        bundle.putString(PushConstant.KEY_LANDING_TARGET_URL, landingTargetUrl)
//                        bundle.putString(PushConstant.KEY_LANDING_CUSTOM_DATA, customData)
//                    }
//
//                    else -> {
//                        Timber.d("landingService else")
//                        return@let
//                    }
//                }
//                putExtras(bundle)
//
//                // Action 정보를 넣지 않은 경우 다중 Intent Extra 정보를 처리하지 못하고 마지막으로 받은 정보로 대체되는 현상 발생
//                action = "${landingType}_${landingTarget}"
//            }
//
//            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//    }

    /**
     * @return Notify ID 획득
     */
    private fun getNotifyId(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    /**
     * @return Notify Tag 획득
     */
    private fun getNotifyTag(): String {
        val newRandom = Random()
        val buf = StringBuffer()
        for (i in 0 until 8) {
            if (newRandom.nextBoolean()) {
                buf.append((newRandom.nextInt(26) + 97).toChar())
            } else {
                buf.append(newRandom.nextInt(10))
            }
        }
        return buf.toString()
    }
}