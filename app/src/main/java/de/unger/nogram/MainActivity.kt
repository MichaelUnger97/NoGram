package de.unger.nogram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import de.unger.net.NoGramCallback
import de.unger.net.NoGramHttpEngine
import de.unger.net.NoGramRequests
import de.unger.nogram.ui.theme.NoGramTheme

class MainActivity : ComponentActivity() {
    private var networkResponse by mutableStateOf("loading...")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoGramTheme {
                NoGramApp(networkResponse)
            }
        }
        val engine = NoGramHttpEngine(applicationContext)
        val nogramCallback = NoGramCallback {
            networkResponse =
                it.keys.map { "${it.headers.asList.component1()} ${it.headers.asList.component2()}" }
                    .joinToString { it }
        }
        NoGramRequests(engine, nogramCallback).home()
    }
}

@Composable
fun NoGramApp(responseText: String) {
    NavigationSuiteScaffold(
        navigationSuiteItems = { /* ... your items ... */ }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Text(
                text = responseText,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}