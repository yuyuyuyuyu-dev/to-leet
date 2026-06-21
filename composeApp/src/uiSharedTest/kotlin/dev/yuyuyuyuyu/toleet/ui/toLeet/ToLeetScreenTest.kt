package dev.yuyuyuyuyu.toleet.ui.toLeet

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

class ToLeetScreenTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `leet text is displayed when input text is entered`() =
        runComposeUiTest {
            setContent {
                ToLeetScreen(viewModel = ToLeetViewModelImpl())
            }

            onNode(hasSetTextAction()).performTextInput("leet")

            onNodeWithText("l33t").assertIsDisplayed()
        }
}
