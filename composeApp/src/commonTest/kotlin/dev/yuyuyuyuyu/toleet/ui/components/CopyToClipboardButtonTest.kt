package dev.yuyuyuyuyu.toleet.ui.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeClipboardManager : ClipboardManager {
    var copiedText: AnnotatedString? = null

    override fun getText(): AnnotatedString? = copiedText

    override fun setText(annotatedString: AnnotatedString) {
        copiedText = annotatedString
    }

    override fun hasText(): Boolean = copiedText != null
}

class CopyToClipboardButtonTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun shouldCopyTextToClipboardWhenClicked() =
        runComposeUiTest {
            val fakeClipboardManager = FakeClipboardManager()
            val textToCopy = "12345"

            setContent {
                CompositionLocalProvider(LocalClipboardManager provides fakeClipboardManager) {
                    CopyToClipboardButton(
                        textToCopy = textToCopy,
                    )
                }
            }

            onNode(hasClickAction()).performClick()

            assertEquals(textToCopy, fakeClipboardManager.copiedText?.text)
        }
}
