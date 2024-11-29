package semi.android.trackcloud.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import semi.android.trackcloud.MainActivity

class NotificationActionReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PLAY = "semi.android.trackcloud.notifications.ACTION_PLAY"
        const val ACTION_PAUSE = "semi.android.trackcloud.notifications.ACTION_PAUSE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val webView = MainActivity.webViewInstance  // Asumiendo que MainActivity guarda una instancia estática del WebView

        when (action) {
            ACTION_PLAY -> {
                webView?.evaluateJavascript(
                    "jQuery('#sound-player').jPlayer('play');",
                    null
                )
            }
            ACTION_PAUSE -> {
                webView?.evaluateJavascript(
                    "jQuery('#sound-player').jPlayer('pause');",
                    null
                )
            }
        }
    }
}

