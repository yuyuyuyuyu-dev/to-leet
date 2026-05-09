package dev.yuyuyuyuyu.toleet.ui.toLeet

import dev.yuyuyuyuyu.toleet.domain.useCase.ToLeetUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ToLeetViewModelTest {
    @Test
    fun `textFieldOnChange should update uiState correctly`() =
        runTest {
            // Arrange
            val useCase = ToLeetUseCase()
            val viewModel = ToLeetViewModelImpl(useCase)

            // Initial state check
            assertEquals("", viewModel.uiState.value.inputText)
            assertEquals("", viewModel.uiState.value.leetText)

            // Act
            val input = "leet"
            viewModel.textFieldOnChange(input)

            // Assert
            assertEquals(input, viewModel.uiState.value.inputText)
            assertEquals("l33t", viewModel.uiState.value.leetText)
        }
}
