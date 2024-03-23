package com.deserve.restaurantsnearme.model.dto

import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.google.gson.annotations.SerializedName

data class SearchPlacesApiResponse(
    @SerializedName("results")
    val results: List<Restaurant>
)

data class PlacePhotoResponse(

    @SerializedName("prefix")
    val prefix: String = "",

    @SerializedName("suffix")
    val suffix: String = ""

)
