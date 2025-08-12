package dev.yuyuyuyuyu.toleet.di

import dev.yuyuyuyuyu.toleet.domain.useCase.ToLeetUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::ToLeetUseCase)
}
