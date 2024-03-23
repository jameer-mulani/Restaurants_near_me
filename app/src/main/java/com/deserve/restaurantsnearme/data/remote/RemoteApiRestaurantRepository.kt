package com.deserve.restaurantsnearme.data.remote

import android.util.Log
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.data.RestaurantRepository
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Restaurant

class RemoteApiRestaurantRepository(private val api: ApiSpec) : RestaurantRepository {
    override suspend fun fetchRestaurant(input: GetRestaurantInput): Resource<List<Restaurant>> {
        val searchPlaces = api.searchPlaces(location = "${input.location.latitude},${input.location.longitude}")
        return if (searchPlaces.isSuccessful) {
            Log.d("repository", "fetchRestaurant: ${searchPlaces.body()}")
            Resource.success(searchPlaces.body()?.results ?: emptyList())
        } else {
            Resource.failed(searchPlaces.errorBody()?.toString() ?: "Failed to fetch restaurants from api")
        }
    }

    override suspend fun fetchRestaurantPhoto(restaurantId: String, width : Int,height : Int): Resource<String> {
        val photoResponse = api.getPhotos(id = restaurantId)
        return when {
            photoResponse.isSuccessful -> {
                val response = photoResponse.body()
                Log.d("repository", "fetchRestaurantPhoto: ${response}")
                if (!response.isNullOrEmpty()) {
                    val url = "${response[0].prefix}${width}x${height}${response[0].suffix}"
                    Resource.success(url)
                } else {
                    Resource.success("")
                }
            }
            else -> {
                Resource.failed(photoResponse.errorBody()?.toString() ?: "")
            }
        }
    }
}