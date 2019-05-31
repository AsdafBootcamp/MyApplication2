package com.narims.myapplication.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class AutocompleteResponse {
    @JsonProperty("predictions")
    var autocompleteItems: List<AutocompleteItem>? = null
}