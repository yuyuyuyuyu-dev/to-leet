package dev.yuyuyuyuyu.toleet.ui.toLeet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.yuyuyuyuyu.toleet.ui.components.CopyToClipboardButton
import org.jetbrains.compose.resources.stringResource
import toleet.composeapp.generated.resources.Res
import toleet.composeapp.generated.resources.to_leet_input_label

@Composable
fun ToLeetScreen(viewModel: ToLeetViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = uiState.inputText,
                    onValueChange = viewModel::textFieldOnChange,
                    label = { Text(stringResource(Res.string.to_leet_input_label)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )

                Text(uiState.leetText)

                CopyToClipboardButton(
                    textToCopy = uiState.leetText,
                    enabled = uiState.inputText.isNotEmpty(),
                    snackbarHostState = snackBarHostState,
                )
            }
        }
    }
}
