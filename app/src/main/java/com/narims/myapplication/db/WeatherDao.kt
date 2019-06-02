package com.narims.myapplication.db

import android.arch.persistence.room.*
import com.narims.myapplication.model.WeatherItem
import io.reactivex.Flowable
import io.reactivex.Maybe


@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatheritem WHERE createdAt > :timestampFrom LIMIT :limit")
    fun getAll(timestampFrom: Long, limit: Int): Flowable<List<WeatherItem>>

    @Query("SELECT * FROM weatheritem LIMIT :limit")
    fun getAllSync(limit: Int): List<WeatherItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherItemList: List<WeatherItem>)

    @Query("DELETE FROM weatheritem")
    fun removeAll()

    @Query("SELECT queryCache FROM weatheritem")
    fun getLastDate(): Maybe<String>
}