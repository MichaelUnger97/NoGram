package de.unger.nogram

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            val previousHtml = webView.getTag(android.R.id.content) as? String
            if (previousHtml != html) {
                webView.setTag(android.R.id.content, html)
                webView.loadDataWithBaseURL(
                    "https://www.instagram.com",
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}

