package semi.android.trackcloud.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.media.app.NotificationCompat.MediaStyle
import semi.android.trackcloud.R
import semi.android.trackcloud.notifications.NotificationHelper.updateNotification

class NotificationService : LifecycleService() {

    private val CHANNEL_ID = "TrackCloudChannel"
    private val NOTIFICATION_ID = 1
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "TrackCloud Notifications",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Controla la reproducción desde la barra de notificaciones"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val playIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_PLAY"
        }
        val playPendingIntent = PendingIntent.getBroadcast(
            this, 100, playIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val pauseIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_PAUSE"
        }
        val pausePendingIntent = PendingIntent.getBroadcast(
            this, 101, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("")
            .setSmallIcon(R.drawable.ic_music_note) // Cambia por tu ícono
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setStyle(MediaStyle().setShowActionsInCompactView(0, 1))
            .addAction(R.drawable.ic_play, "Reproducir", playPendingIntent)
            .addAction(R.drawable.ic_pause, "Pausar", pausePendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val songTitle = intent?.getStringExtra("SONG_TITLE")

        if (!songTitle.isNullOrEmpty()) {
            updateNotification(this, songTitle, isPlaying = true)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // Detener el servicio en primer plano y cancelar la notificación
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}

