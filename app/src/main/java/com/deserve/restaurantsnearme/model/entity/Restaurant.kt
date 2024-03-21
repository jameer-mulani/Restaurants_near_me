package com.deserve.restaurantsnearme.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Restaurant(

    @SerializedName("fsq_id")
    val id: String,
    @SerializedName("name")
    val name: String,

    var imageUrl: String? = "",

    @SerializedName("closed_bucket")
    val openStatus: String,

    @SerializedName("location")
    val location: RestaurantLocation
) : Parcelable

@Parcelize
data class RestaurantLocation(
    @SerializedName("formatted_address")
    val formattedAddress: String? = "NA"
) : Parcelable

data class RestaurantGeoCodes(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
