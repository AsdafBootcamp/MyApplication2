package com.narims.myapplication.di

import com.narims.myapplication.presentation.mvp.itemlist.ItemListPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface MainComponent {
    fun inject(itemListPresenter: ItemListPresenter)
}