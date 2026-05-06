package dev.yuyuyuyuyu.toleet

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import dev.yuyuyuyuyu.toleet.ui.main.MainScreen
import dev.yuyuyuyuyu.mymaterialtheme.MyMaterialTheme

@Composable
@Preview
fun App() {
    MyMaterialTheme {
        MainScreen()
    }
}
