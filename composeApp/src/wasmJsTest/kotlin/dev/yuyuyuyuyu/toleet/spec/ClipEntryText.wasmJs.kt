@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)

package dev.yuyuyuyuyu.toleet.spec

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import kotlinx.coroutines.await
import org.w3c.fetch.Response
import org.w3c.files.Blob

actual suspend fun ClipEntry.readPlainText(): String {
    val blob =
        checkNotNull(clipboardItems[0])
            .getType("text/plain".toJsString())
            .await<Blob>()
    return Response(blob).text().await<JsString>().toString()
}
