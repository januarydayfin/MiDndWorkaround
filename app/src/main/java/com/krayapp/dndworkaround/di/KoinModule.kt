package com.krayapp.dndworkaround.di

import com.krayapp.dndworkaround.data.DndRepository
import com.krayapp.dndworkaround.data.DndRepositoryImpl
import com.krayapp.dndworkaround.MainViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DndRepositoryImpl) { bind<DndRepository>() }
    viewModelOf(::MainViewModel)
}