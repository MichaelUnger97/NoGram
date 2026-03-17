package de.unger.nogram

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.unger.net.NoGramCallback
import de.unger.net.NoGramHttpEngine
import de.unger.net.NoGramRequests
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoGramViewModel(application: Application) : AndroidViewModel(application) {
    private val engine = NoGramHttpEngine(getApplication())

    private val _uiState = MutableStateFlow("loading...")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    fun home() {
        _uiState.value = "loading..."

        val callback = NoGramCallback(
            onSuccess = { response ->
                _uiState.value = response
            },
            onError = { error ->
                _uiState.value = "Error: $error"
            }
        )

        NoGramRequests(engine, callback).home()
    }
}