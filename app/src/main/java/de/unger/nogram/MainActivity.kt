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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
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
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadsImagesAutomatically = true
                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            val previousHtml = webView.tag as? String
            if (previousHtml != html) {
                webView.tag = html
                webView.loadDataWithBaseURL(
                    "https://www.instagram.com/",
                    html,
                    "text/html",
                    "UTF-8",
                    "https://www.instagram.com/"

                )
            }
        }
    )
}

