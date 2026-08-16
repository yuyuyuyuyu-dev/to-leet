package dev.yuyuyuyuyu.toleet.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import dev.yuyuyuyuyu.mycomposables.MyScaffold
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetScreen
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetViewModelImpl
import org.jetbrains.compose.resources.stringResource
import toleet.composeapp.generated.resources.Res
import toleet.composeapp.generated.resources.app_name

@Composable
fun MainScreen() {
    val focusManager = LocalFocusManager.current
    val uriHandler = LocalUriHandler.current

    val libraries by produceLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    MyScaffold(
        title = stringResource(Res.string.app_name),
        libraries = libraries?.let { libs -> libs.copy(libraries = libs.libraries.distinctBy { it.name }) },
        modifier =
            Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = { focusManager.clearFocus() },
            ),
        onSourceCodeButtonClick = {
            uriHandler.openUri("https://github.com/yuyuyuyuyu-dev/to-leet")
        },
    ) { innerPadding ->
        ToLeetScreen(ToLeetViewModelImpl(), Modifier.padding(innerPadding))
    }
}
