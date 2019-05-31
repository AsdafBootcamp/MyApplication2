package com.narims.myapplication.presentation.mvp.itemlist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.narims.myapplication.model.WeatherItem

@StateStrategyType(AddToEndSingleStrategy::class)
interface ItemListView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setSearchQuery(query: String)

    fun updateList(newList: List<WeatherItem>)
    fun updateListWithDiff(newList: List<WeatherItem>)
    fun registerTextChanges()
}