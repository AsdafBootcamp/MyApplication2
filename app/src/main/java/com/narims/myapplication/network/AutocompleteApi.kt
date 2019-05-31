package com.narims.myapplication.network

import com.narims.myapplication.model.AutocompleteResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

interface AutocompleteApi {
    companion object {
        const val API_KEY = "AIzaSyC58crCEc0niZJnBLT8Wri9_v8z8iEFmr8"
        const val TYPES = "(cities)"
        const val OFFSET = "3"
    }

    @GET("maps/api/place/autocomplete/json?key=$API_KEY&types=$TYPES&offset=$OFFSET")
    fun getAutocomplete(@Query("input") input: String): Maybe<AutocompleteResponse>
}