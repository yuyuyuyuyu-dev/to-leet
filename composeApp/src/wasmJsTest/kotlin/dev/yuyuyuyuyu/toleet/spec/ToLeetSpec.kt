package dev.yuyuyuyuyu.toleet.spec

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
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
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import org.w3c.dom.Document
import org.w3c.dom.parsing.DOMParser
import org.w3c.fetch.Response
import org.w3c.files.Blob
import toleet.composeapp.generated.resources.Res
import toleet.composeapp.generated.resources.copy_to_clipboard
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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

    @Test
    fun `should ask browsers not to offer to translate the page`() =
        runTest {
            // Act
            val page = loadPage()

            // Assert
            val root = checkNotNull(page.documentElement)
            assertEquals("no", root.getAttribute("translate"))
            assertTrue(root.classList.contains("notranslate"))
            assertNotNull(page.querySelector("meta[name='google'][content='notranslate']"))
        }
}

@OptIn(ExperimentalWasmJsInterop::class)
private suspend fun loadPage(): Document {
    val html =
        window
            .fetch("/base/kotlin/index.html")
            .await<Response>()
            .text()
            .await<JsString>()
            .toString()
    return DOMParser().parseFromString(html, "text/html".toJsString())
}

private class FakeClipboard : Clipboard {
    var clipEntry: ClipEntry? = null

    override suspend fun getClipEntry(): ClipEntry? = clipEntry

    override suspend fun setClipEntry(clipEntry: ClipEntry?) {
        this.clipEntry = clipEntry
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)
private suspend fun ClipEntry.readPlainText(): String {
    val blob =
        checkNotNull(clipboardItems[0])
            .getType("text/plain".toJsString())
            .await<Blob>()
    return Response(blob).text().await<JsString>().toString()
}
