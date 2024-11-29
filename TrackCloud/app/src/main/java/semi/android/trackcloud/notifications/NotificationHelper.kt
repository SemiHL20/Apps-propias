package semi.android.trackcloud.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import semi.android.trackcloud.R

object NotificationHelper {
    private const val CHANNEL_ID = "TrackCloudChannel"
    private const val NOTIFICATION_ID = 1

    fun updateNotification(context: Context, songTitle: String, isPlaying: Boolean) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Acción de Play
        val playIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = NotificationActionReceiver.ACTION_PLAY
        }
        val playPendingIntent = PendingIntent.getBroadcast(
            context, 100, playIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Acción de Pause
        val pauseIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = NotificationActionReceiver.ACTION_PAUSE
        }
        val pausePendingIntent = PendingIntent.getBroadcast(
            context, 101, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText(songTitle) // Mostrar el título de la canción
            .setSmallIcon(R.drawable.ic_music_note)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setStyle(MediaStyle().setShowActionsInCompactView(0, 1)) // Mostrar botones en modo compacto
            .addAction(R.drawable.ic_play, "Play", playPendingIntent) // Botón Play
            .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent) // Botón Pause
            .setOngoing(true)
            .build()

        // Actualizar la notificación
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
