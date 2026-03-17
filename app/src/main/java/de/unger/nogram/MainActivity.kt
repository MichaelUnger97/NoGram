package de.unger.nogram

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import de.unger.nogram.ui.theme.NoGramTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoGramTheme {
                HtmlDisplay()
            }
        }
    }


    @Composable
    fun HtmlDisplay() {
        androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxSize()) {

            Button(
                onClick = {
                    val webView = this@MainActivity.findViewById<WebView>(R.id.webview)

                    // Convert the space-separated classes into a CSS selector (e.g., .class1.class2.class3)
                    // JavaScript to find the specific last child and click it
                    val script = """
    (function() {
        var snapContainer = Array.from(document.querySelectorAll('div')).find(el => {
            var style = window.getComputedStyle(el);            
            return style.scrollSnapType.includes('mandatory');
        });

        if (snapContainer) {
            // First half
            snapContainer.scrollBy({
                top: snapContainer.clientHeight *2/ 3,
                behavior: 'smooth'
            });

        }
    })();
""".trimIndent()
                    webView.evaluateJavascript(script, null)
                },
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            ) {
                Text("Hide Ads")
            }
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        id = R.id.webview
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadsImagesAutomatically = true
                        settings.userAgentString =
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
                        setInitialScale(250)
                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(this, true)

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                // Save cookies to disk when a page finishes loading
                                cookieManager.flush()
                            }
                        }
                        loadUrl("https://www.instagram.com/")
                    }
                }
            )

        }
    }
}
