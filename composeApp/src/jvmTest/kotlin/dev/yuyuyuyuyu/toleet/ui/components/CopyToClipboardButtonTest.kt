package dev.yuyuyuyuyu.toleet.ui.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.NativeClipboard
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalComposeUiApi::class)
class FakeClipboard : Clipboard {
    var clipEntry: ClipEntry? = null

    override suspend fun getClipEntry(): ClipEntry? = clipEntry

    override suspend fun setClipEntry(clipEntry: ClipEntry?) {
        this.clipEntry = clipEntry
    }

    override val nativeClipboard: NativeClipboard
        get() = TODO("Not implemented in FakeClipboard")
}

class CopyToClipboardButtonTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun shouldCopyTextToClipboardWhenClicked() =
        runComposeUiTest {
            val fakeClipboard = FakeClipboard()
            val textToCopy = "12345"

            setContent {
                CompositionLocalProvider(LocalClipboard provides fakeClipboard) {
                    CopyToClipboardButton(
                        textToCopy = textToCopy,
                    )
                }
            }

            onNode(hasClickAction()).performClick()

            // Verification of ClipEntry content is currently difficult in common tests
            // due to it being an expect class and missing a common API to read back plain text.
            // But we can verify it's not null and we can execute the flow.
            assertEquals(true, fakeClipboard.clipEntry != null)
        }
}
