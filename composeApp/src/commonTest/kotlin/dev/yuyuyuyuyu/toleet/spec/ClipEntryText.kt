package dev.yuyuyuyuyu.toleet.spec

import androidx.compose.ui.platform.ClipEntry

expect suspend fun ClipEntry.readPlainText(): String
