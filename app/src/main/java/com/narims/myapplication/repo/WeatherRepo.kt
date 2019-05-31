package com.narims.myapplication.repo

import com.narims.myapplication.model.WeatherItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface WeatherRepo {
    fun getCachedItems(): Flowable<List<WeatherItem>>

    fun queryWeatherItem(query: String): Completable

    fun getQueryCache(): Single<String>

    fun removeAll(): Completable
}