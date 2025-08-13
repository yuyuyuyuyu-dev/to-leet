package dev.yuyuyuyuyu.toleet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.yuyuyuyuyu.genkaikor.ui.openSourceLicenseList.OpenSourceLicenseListScreen
import dev.yuyuyuyuyu.mymaterialtheme.MyMaterialTheme
import dev.yuyuyuyuyu.simpleTopAppBar.SimpleTopAppBar
import dev.yuyuyuyuyu.toleet.di.domainModule
import dev.yuyuyuyuyu.toleet.di.uiModule
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetScreen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import toleet.composeapp.generated.resources.Res
import toleet.composeapp.generated.resources.app_name
import toleet.composeapp.generated.resources.open_source_licenses

@Composable
@Preview
fun ToLeetApp() {
    val backStack = rememberSaveableBackStack(root = ToLeetScreen)
    val navigator = rememberCircuitNavigator(backStack) {}
    val currentScreen = backStack.topRecord?.screen

    val focusManager = LocalFocusManager.current
    val uriHandler = LocalUriHandler.current

    KoinApplication(
        application = {
            printLogger()
            modules(uiModule, domainModule)
        },
    ) {
        MyMaterialTheme {
            Scaffold(
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { focusManager.clearFocus() },
                ),
                topBar = {
                    SimpleTopAppBar(
                        title = when (currentScreen) {
                            is OpenSourceLicenseListScreen -> stringResource(Res.string.open_source_licenses)
                            else -> stringResource(Res.string.app_name)
                        },
                        navigateBackIsPossible = backStack.size > 1,
                        onNavigateBackButtonClick = {
                            navigator.pop()
                        },
                        onOpenSourceLicensesButtonClick = {
                            navigator.goTo(OpenSourceLicenseListScreen)
                        },
                        onSourceCodeButtonClick = {
                            uriHandler.openUri("https://github.com/yuyuyuyuyu-dev/to-leet")
                        },
                    )
                },
            ) { paddingValues ->
                CircuitCompositionLocals(circuit = koinInject()) {
                    NavigableCircuitContent(navigator, backStack, Modifier.padding(paddingValues))
                }
            }
        }
    }
}
