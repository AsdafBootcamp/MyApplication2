package com.narims.myapplication.repo

import com.narims.myapplication.db.WeatherDao
import com.narims.myapplication.model.WeatherItem
import com.narims.myapplication.network.AutocompleteApi
import com.narims.myapplication.network.WeatherApi
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.IOException

class MultiSourceWeatherRepo(
    private val autocompleteApi: AutocompleteApi,
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) : WeatherRepo {
    companion object {
        const val WEATHER_ITEMS_LIMIT = 10
        const val CACHE_LIFETIME = 1000 * 60 * 60
    }

    override fun getCachedItems(): Flowable<List<WeatherItem>> {
        return weatherDao.getAll(getTimeFrom(), WEATHER_ITEMS_LIMIT)
    }

    override fun queryWeatherItem(query: String): Completable {
        return Completable.fromMaybe(autocompleteApi.getAutocomplete(query)
            .observeOn(Schedulers.io())
            .doOnSuccess {
                val weatherItems = ArrayList<WeatherItem>()
                val createdAt = System.currentTimeMillis()
                it.autocompleteItems?.forEach { item ->
                    item.cityName?.let { name ->
                        syncWeatherItemInsert(name, query, createdAt)?.let { weather ->
                            weatherItems.add(weather)
                        }
                    }
                }
                if (weatherItems.isNotEmpty())
                    weatherDao.insert(weatherItems)
                else {
                    weatherDao.removeAll()
                }
            })
    }

    override fun getQueryCache(): Single<String> {
        return Single.fromCallable {
            weatherDao.getAllSync(1)[0].queryCache
        }
    }

    override fun removeAll(): Completable {
        return Completable.fromAction {
            weatherDao.removeAll()
        }
    }

    private fun syncWeatherItemInsert(cityName: String, query: String, createdAt: Long): WeatherItem? {
        try {
            return saveWeatherItemResponse(weatherApi.getWeather(cityName).execute(), cityName, query, createdAt)
        } catch (e: IOException) {
        }
        return null
    }

    private fun saveWeatherItemResponse(
        weatherItemResponse: Response<WeatherItem>,
        cityName: String,
        query: String,
        createdAt: Long
    ): WeatherItem? {
        var result: WeatherItem? = null
        if (weatherItemResponse.isSuccessful) {
            weatherItemResponse.body()?.let { weatherItem ->
                weatherItem.createdAt = createdAt
                weatherItem.cityName = cityName
                weatherItem.queryCache = query.split(",")[0]
                result = weatherItem
            }
        }
        return result
    }

    private fun getTimeFrom(): Long {
        return System.currentTimeMillis() - CACHE_LIFETIME
    }
}