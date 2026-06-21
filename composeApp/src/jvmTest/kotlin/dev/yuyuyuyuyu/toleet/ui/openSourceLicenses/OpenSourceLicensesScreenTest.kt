package dev.yuyuyuyuyu.toleet.ui.openSourceLicenses

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class OpenSourceLicensesScreenTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `screen renders without crashing`() =
        runComposeUiTest {
            setContent {
                OpenSourceLicensesScreen()
            }

            // Wait for idle is done automatically by runComposeUiTest.
            // If it doesn't crash, the test passes.
        }
}
