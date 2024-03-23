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
    val location: RestaurantLocation,

    @SerializedName("geocodes")
    val geoCodes: RestaurantGeoCodes

) : Parcelable

@Parcelize
data class RestaurantLocation(
    @SerializedName("formatted_address")
    val formattedAddress: String? = "NA"
) : Parcelable


@Parcelize
data class RestaurantGeoCodes(
    @SerializedName("main")
    val restaurantGeoCodeMain: RestaurantGeoCodeMain
): Parcelable

@Parcelize
data class RestaurantGeoCodeMain(
    val latitude : Double,
    val longitude : Double
) : Parcelable
