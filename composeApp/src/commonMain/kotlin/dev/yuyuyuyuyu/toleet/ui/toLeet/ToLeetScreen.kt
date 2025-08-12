package dev.yuyuyuyuyu.toleet.ui.toLeet

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

data object ToLeetScreen : Screen {
    data class State(
        val leetText: String,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class InputValueChanged(val newValue: String) : Event()
    }
}
