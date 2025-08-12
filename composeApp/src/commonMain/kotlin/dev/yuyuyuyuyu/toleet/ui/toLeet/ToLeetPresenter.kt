package dev.yuyuyuyuyu.toleet.ui.toLeet

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.presenter.Presenter

class ToLeetPresenter : Presenter<ToLeetScreen.State> {
    @Composable
    override fun present(): ToLeetScreen.State {
        return ToLeetScreen.State
    }
}
