package com.narims.myapplication.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherItem : Serializable {
    @JsonIgnore
    @PrimaryKey
    var cityName: String = ""
    @JsonIgnore
    var temperature: String = ""
    @JsonIgnore
    var createdAt: Long? = null
    @JsonIgnore
    var queryCache: String? = null

    @JsonProperty("main")
    fun setTemperature(main: Map<String, String>) {
        main["temp"]?.let {
            temperature = it
        }
    }
}