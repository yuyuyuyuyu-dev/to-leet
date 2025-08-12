package dev.yuyuyuyuyu.toleet.ui.toLeet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.presenter.Presenter
import dev.yuyuyuyuyu.toleet.domain.useCase.ToLeetUseCase

class ToLeetPresenter(
    private val toLeetUseCase: ToLeetUseCase,
) : Presenter<ToLeetScreen.State> {
    @Composable
    override fun present(): ToLeetScreen.State {
        var leetText by rememberSaveable { mutableStateOf("") }

        return ToLeetScreen.State(leetText) { event ->
            when (event) {
                is ToLeetScreen.Event.InputValueChanged -> {
                    leetText = toLeetUseCase(event.newValue)
                }
            }
        }
    }
}
