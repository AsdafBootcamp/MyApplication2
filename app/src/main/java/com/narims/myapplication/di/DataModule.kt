package com.narims.myapplication.di

import android.app.Application
import android.arch.persistence.room.Room
import com.narims.myapplication.db.Database
import com.narims.myapplication.db.WeatherDao
import com.narims.myapplication.network.AutocompleteApi
import com.narims.myapplication.network.WeatherApi
import com.narims.myapplication.network.createRetrofit
import com.narims.myapplication.repo.WeatherRepo
import com.narims.myapplication.repo.MultiSourceWeatherRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(private var application: Application) {
    @Provides
    @Singleton
    fun providesWeatherDao(): WeatherDao {
        return Room.databaseBuilder(application, Database::class.java, "weather_items")
            .build()
            .weatherDao()
    }

    @Provides
    @Singleton
    fun providesWeatherApi(): WeatherApi {
        return createRetrofit("https://api.openweathermap.org/")
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAutocompleteApi(): AutocompleteApi {
        return createRetrofit("https://maps.googleapis.com/")
            .create(AutocompleteApi::class.java)
    }

    @Provides
    @Singleton
    fun providesWeatherRepo(
        autocompleteApi: AutocompleteApi,
        weatherApi: WeatherApi,
        weatherDao: WeatherDao
    ): WeatherRepo {
        return MultiSourceWeatherRepo(autocompleteApi, weatherApi, weatherDao)
    }
}