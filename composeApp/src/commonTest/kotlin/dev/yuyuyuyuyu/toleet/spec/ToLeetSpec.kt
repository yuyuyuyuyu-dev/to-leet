package dev.yuyuyuyuyu.toleet.spec

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import dev.yuyuyuyuyu.toleet.App
import org.jetbrains.compose.resources.getString
import toleet.composeapp.generated.resources.Res
import toleet.composeapp.generated.resources.copy_to_clipboard
import kotlin.test.Test
import kotlin.test.assertEquals

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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `should copy the leet text to the clipboard when the copy button is clicked`() =
        runComposeUiTest {
            // Arrange
            val clipboard = FakeClipboard()
            val copyButtonLabel = getString(Res.string.copy_to_clipboard)
            setContent {
                CompositionLocalProvider(LocalClipboard provides clipboard) {
                    App()
                }
            }
            onNode(hasSetTextAction()).performTextInput("leet")
            waitUntilAtLeastOneExists(hasText(copyButtonLabel))

            // Act
            onNodeWithText(copyButtonLabel).performClick()
            waitUntil { clipboard.clipEntry != null }

            // Assert
            assertEquals("l33t", clipboard.clipEntry?.readPlainText())
        }
}

private class FakeClipboard : Clipboard {
    var clipEntry: ClipEntry? = null

    override suspend fun getClipEntry(): ClipEntry? = clipEntry

    override suspend fun setClipEntry(clipEntry: ClipEntry?) {
        this.clipEntry = clipEntry
    }
}
