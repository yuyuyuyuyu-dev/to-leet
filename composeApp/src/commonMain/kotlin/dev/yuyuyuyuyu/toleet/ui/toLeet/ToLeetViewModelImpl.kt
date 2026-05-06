package dev.yuyuyuyuyu.toleet.ui.toLeet

import androidx.lifecycle.ViewModel
import dev.yuyuyuyuyu.toleet.domain.useCase.ToLeetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ToLeetViewModelImpl(
    private val toLeetUseCase: ToLeetUseCase = ToLeetUseCase()
) : ViewModel(), ToLeetViewModel {

    private val _uiState = MutableStateFlow(ToLeetUiState(inputText = "", leetText = ""))
    override val uiState: StateFlow<ToLeetUiState> = _uiState.asStateFlow()

    override fun textFieldOnChange(newValue: String) {
        val leetText = toLeetUseCase(newValue)
        _uiState.update { 
            it.copy(
                inputText = newValue,
                leetText = leetText
            ) 
        }
    }
}
