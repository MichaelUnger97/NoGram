package de.unger.nogram

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import de.unger.nogram.ui.theme.NoGramTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: NoGramViewModel by viewModels()
        // 1. Call setContent ONCE
        setContent {
            // 2. Use collectAsState to observe changes safely in Compose
            val htmlState by viewModel.uiState.collectAsState()
            NoGramTheme {
                NoGramApp(htmlState)
            }
        }
        viewModel.home()
    }

    @Composable
    fun NoGramApp(htmlState: String) {
        when {
            htmlState.startsWith("loading", ignoreCase = true) || htmlState.isBlank() -> {
                Text("Loading Content...")
            }

            htmlState.startsWith("Error:") -> {
                Text(htmlState)
            }

            else -> {
                HtmlDisplay(html = htmlState)
            }
        }
    }

    @Composable
    fun HtmlDisplay(html: String) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                    }
                },
                update = { webView ->
                    val previousHtml = webView.tag as? String
                    if (previousHtml != html) {
                        webView.tag = html
                        /* webView.loadDataWithBaseURL(
                             "https://www.instagram.com/",
                             html,
                             "text/html",
                             "UTF-8",
                             "https://www.instagram.com/"

                         )
                         */
                        webView.loadUrl("https://www.instagram.com/")
                    }
                }
            )
            Button(
                onClick = {
                    val webView = this@MainActivity.findViewById<WebView>(R.id.webview)

                    // Convert the space-separated classes into a CSS selector (e.g., .class1.class2.class3)
                    // JavaScript to find the specific last child and click it
                    val script = """
    (function() {
        // Find the element that handles the snap-scrolling
        var snapContainer = Array.from(document.querySelectorAll('div')).find(el => {
            var style = window.getComputedStyle(el);
            return style.scrollSnapType.includes('mandatory');
        });

        if (snapContainer) {
            // Instagram Reels often use a specific height for each child.
            // You can trigger a scroll to the next "snap" point manually:
            snapContainer.scrollBy({
                top: snapContainer.clientHeight,
                behavior: 'smooth'
            });
        }
    })();
""".trimIndent()

                    webView.evaluateJavascript(script, null)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp)
                    .zIndex(1f)
            ) {
                Text("Hide Ads")
            }
        }
    }
}
