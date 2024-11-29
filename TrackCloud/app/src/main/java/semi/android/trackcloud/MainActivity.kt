package semi.android.trackcloud

import WebAppInterface
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import semi.android.trackcloud.notifications.NotificationService
import semi.android.trackcloud.ui.theme.TrackCloudTheme
import semi.android.trackcloud.web.MyWebViewClient

class MainActivity : ComponentActivity() {

    private lateinit var configuration: Configuration

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                moveTaskToBack(true) // Mueve la app a segundo plano
            }
        }
    }

    companion object {
        var webViewInstance: WebView? = null // Referencia estática del WebView
    }

    private val webView: WebView by lazy {
        WebView(this).apply {
            webViewClient = MyWebViewClient()
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebAppInterface(this@MainActivity), "AndroidBridge")

            settings.domStorageEnabled = true // Habilitar almacenamiento DOM
            settings.javaScriptCanOpenWindowsAutomatically = true // Permite ventanas emergentes
            settings.setSupportMultipleWindows(true) // Permite múltiples ventanas
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        webViewInstance = webView // Asignar el WebView a la referencia estática

        Intent(this, NotificationService::class.java).also {
            startForegroundService(it)
        }

        configuration = resources.configuration

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setContent {
            val currentUrl by rememberSaveable {
                mutableStateOf("https://trackcloud.es/") }
            TrackCloudTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding ->
                        WebScreen(
                            url = currentUrl,
                            webView = webView,
                            modifier = Modifier.padding(innerPadding)
                        )
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("currentUrl", webView.url)
        webView.saveState(outState)
        configuration = resources.configuration
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        webView.restoreState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.stopLoading()
        webView.destroy()
        webViewInstance = null
        onBackPressedCallback.remove()
        stopService(Intent(this, NotificationService::class.java))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebScreen(url: String, webView: WebView, modifier : Modifier = Modifier) {
    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize(),
        update = { it.loadUrl(url) } // Carga la URL en el WebView existente
    )
}
