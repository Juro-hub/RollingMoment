package kr.co.rolling.moment.feature.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kr.co.rolling.moment.R
import kr.co.rolling.moment.feature.MomentActivity
import kr.co.rolling.moment.library.data.Constants.MOMENT_PUSH_DATA_KEY
import kr.co.rolling.moment.library.data.Constants.MOMENT_PUSH_KEY_BODY
import kr.co.rolling.moment.library.data.Constants.MOMENT_PUSH_KEY_PAYLOAD
import kr.co.rolling.moment.library.data.Constants.MOMENT_PUSH_KEY_TITLE
import kr.co.rolling.moment.library.data.NavigationResponse
import kr.co.rolling.moment.library.data.toEntity
import kr.co.rolling.moment.library.util.PreferenceManager
import kr.co.rolling.moment.library.util.htmlToSpanned
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

        message.data.let { notification ->
            Timber.d("notification : ${notification}")
            Timber.d("notification2 : ${notification.entries}")
            var title = notification[MOMENT_PUSH_KEY_TITLE]
            if (title.isNullOrEmpty()) {
                title = getString(R.string.app_name)
            }
            val body = notification[MOMENT_PUSH_KEY_BODY]

            sendNotification(title, body, createIntent(notification))
        }
    }

    /**
     * 노티피케이션 설정
     */
    private fun sendNotification(title: String?, body: String?, intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(this, getNotifyId(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val manager: NotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentText(body?.htmlToSpanned())
            .setContentIntent(pendingIntent)
        manager.notify(getNotifyTag(), getNotifyId(), builder.build())
    }

    /**
     * 이동 할 Intent 정보 생성
     *
     * IntroActivity로 Intent 정보 받을 수 있도록 설정
     */
    private fun createIntent(map: Map<String, String>?): Intent {
        return Intent(this, MomentActivity::class.java).apply {
            map?.let {
                val payload = map[MOMENT_PUSH_KEY_PAYLOAD]

                val bundle = Bundle()
                val customData = Gson().fromJson(payload, NavigationResponse::class.java).toEntity()
                Timber.d("PushNotificationService Payload : $customData")
                bundle.putParcelable(MOMENT_PUSH_DATA_KEY, customData)

                putExtras(bundle)
            }

            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

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