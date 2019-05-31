package com.narims.myapplication.util

import android.support.annotation.Nullable
import android.support.v7.util.DiffUtil
import com.narims.myapplication.model.WeatherItem


class WeatherItemsDiffCallback(
    private var newWeatherItems: List<WeatherItem>,
    private var oldWeatherItems: List<WeatherItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldWeatherItems.size
    }

    override fun getNewListSize(): Int {
        return newWeatherItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldWeatherItems[oldItemPosition].cityName == newWeatherItems[newItemPosition].cityName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldWeatherItems[oldItemPosition].temperature == newWeatherItems[newItemPosition].temperature
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}