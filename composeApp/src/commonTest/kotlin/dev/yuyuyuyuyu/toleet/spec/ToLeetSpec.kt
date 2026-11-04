package dev.yuyuyuyuyu.toleet.spec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.toleet.App
import kotlin.test.Test

class ToLeetSpec {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `should display A-Z and a-z in leet when they are entered`() =
        runComposeUiTest {
            // Arrange
            setContent {
                App()
            }

            // Act
            onNode(hasSetTextAction()).performTextInput("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")

            // Assert
            onNodeWithText("4BCD3FGH1JKLMN0PQRSTUVWXYZ4bcd3fgh1jklmn0pqrstuvwxyz").assertIsDisplayed()
        }
}
