package com.narims.myapplication.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class AutocompleteItem : Serializable {
    @JsonIgnore
    var cityName: String? = null

    @JsonProperty("structured_formatting")
    fun setCityName(data: Map<String, Any>) {
        data["main_text"]?.let {
            cityName = it.toString()
        }
    }
}