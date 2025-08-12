package dev.yuyuyuyuyu.toleet.di

import com.slack.circuit.foundation.Circuit
import dev.yuyuyuyuyu.genkaikor.ui.openSourceLicenseList.OpenSourceLicenseList
import dev.yuyuyuyuyu.genkaikor.ui.openSourceLicenseList.OpenSourceLicenseListPresenter
import dev.yuyuyuyuyu.genkaikor.ui.openSourceLicenseList.OpenSourceLicenseListScreen
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeet
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetPresenter
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetScreen
import org.koin.dsl.module

val uiModule = module {
    single {
        Circuit.Builder()

            .addPresenter<ToLeetScreen, ToLeetScreen.State>(
                ToLeetPresenter(toLeetUseCase = get())
            )
            .addUi<ToLeetScreen, ToLeetScreen.State> { state, modifier ->
                ToLeet(state, modifier)
            }

            .addPresenter<OpenSourceLicenseListScreen, OpenSourceLicenseListScreen.State>(
                OpenSourceLicenseListPresenter()
            )
            .addUi<OpenSourceLicenseListScreen, OpenSourceLicenseListScreen.State> { _, modifier ->
                OpenSourceLicenseList(modifier)
            }

            .build()
    }
}
