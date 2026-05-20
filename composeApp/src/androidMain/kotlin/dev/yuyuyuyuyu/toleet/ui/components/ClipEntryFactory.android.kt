package dev.yuyuyuyuyu.toleet.ui.components

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

actual fun createClipEntry(text: String): ClipEntry = ClipData.newPlainText(null, text).toClipEntry()
