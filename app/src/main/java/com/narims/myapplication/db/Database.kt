package com.narims.myapplication.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.narims.myapplication.model.WeatherItem

@Database(entities = [WeatherItem::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}