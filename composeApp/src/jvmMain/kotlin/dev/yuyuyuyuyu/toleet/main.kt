package dev.yuyuyuyuyu.toleet

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.yuyuyuyuyu.toleet.ui.ToLeetApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ToLeet",
    ) {
        ToLeetApp()
    }
}