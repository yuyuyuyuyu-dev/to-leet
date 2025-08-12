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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
fun ToLeet(state: ToLeetScreen.State, modifier: Modifier = Modifier) {
    var inputText by rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    value = inputText,
                    onValueChange = { newValue ->
                        inputText = newValue
                        state.eventSink(ToLeetScreen.Event.InputValueChanged(newValue))
                    },
                    label = { Text(stringResource(Res.string.to_leet_input_label)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )

                Text(state.leetText)

                CopyToClipboardButton(
                    textToCopy = inputText,
                    enabled = inputText.isNotEmpty(),
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}
