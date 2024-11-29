package semi.android.trackcloud.web

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

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)

        // Inyectar el JavaScript después de cargar la página
        view.evaluateJavascript(
            """
            (function monitorTrackTitle() {
                setInterval(function() {
                    var currentTitle = document.querySelector('#sw-song-name')?.textContent;
                    if (currentTitle) {
                        window.AndroidBridge.updateSongTitle(currentTitle.trim());
                    } else {
                        console.log("No se encontró el título de la canción.");
                        window.AndroidBridge.updateSongTitle("");
                    }
                }, 1000); // Monitorear cada segundo
            })();
            """, null
        )
        view.evaluateJavascript(
            """
            (function() {
                var cookieDialog = document.querySelector('#aviso-cookies');
                if (cookieDialog) {
                    var links = cookieDialog.querySelectorAll('a');
                    links.forEach(function(link) {
                        link.addEventListener('click', function(event) {
                            event.preventDefault();
                            var href = this.href;
                            cookieDialog.style.display = 'none';
                            window.location.href = href;
                        });
                    });
                    window.addEventListener('popstate', function() {
                        cookieDialog.style.display = 'block';
                    });
                }
            })();
            """,
            null
        )
    }
}

