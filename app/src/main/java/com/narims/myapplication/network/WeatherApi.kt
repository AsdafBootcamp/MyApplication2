package com.narims.myapplication.network

import com.narims.myapplication.model.WeatherItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        const val APP_ID = "2b23c90617924978d10d90ccde4a9041"
    }

    @GET("data/2.5/weather?appid=$APP_ID&units=metric")
    fun getWeather(@Query("q") cityName: String): Call<WeatherItem>
}