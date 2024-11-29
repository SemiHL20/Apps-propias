import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import semi.android.trackcloud.notifications.NotificationService

class WebAppInterface(private val context: Context) {

    @JavascriptInterface
    fun updateSongTitle(songTitle: String) {
        Log.d("WebAppInterface", "Título recibido: $songTitle")
        if (songTitle.isNotEmpty()) {
            val intent = Intent(context, NotificationService::class.java).apply {
                putExtra("SONG_TITLE", songTitle)
            }
            context.startService(intent)
        }
    }
}
