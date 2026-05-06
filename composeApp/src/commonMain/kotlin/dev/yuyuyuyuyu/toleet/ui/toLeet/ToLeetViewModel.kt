package dev.yuyuyuyuyu.toleet.ui.toLeet

import kotlinx.coroutines.flow.StateFlow

interface ToLeetViewModel {
    val uiState: StateFlow<ToLeetUiState>

    fun textFieldOnChange(newValue: String)
}
