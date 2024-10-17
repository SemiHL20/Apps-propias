package semi.android.trackcloud

import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView,
                      request: WebResourceRequest): Boolean {
        // Verifica si la URL es segura
        return if (isSafeUrl(request.url)) {
            // Carga la URL en el WebView
            false
        } else {
            // Abre navegador por defecto (posibilita los anuncios)
            view.context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
            true
        }
    }

    private fun isSafeUrl(url: Uri): Boolean {
        // Lógica para verificar si la URL es segura
        return url.scheme == "https" && url.host == "trackcloud.es"
    }
}