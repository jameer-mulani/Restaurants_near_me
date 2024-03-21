package com.deserve.restaurantsnearme.data.remote

import android.util.Log
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.data.RestaurantRepository
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Restaurant

class RemoteApiRestaurantRepository(private val api : ApiSpec) : RestaurantRepository {
    override suspend fun fetchRestaurant(input: GetRestaurantInput): Resource<List<Restaurant>> {
        val searchPlaces = api.searchPlaces()
        return if (searchPlaces.isSuccessful){
            Log.d("repository", "fetchRestaurant: ${searchPlaces.body()}")
            Resource.success(searchPlaces.body()?.results ?: emptyList())
        }else{
            Resource.failed(searchPlaces.errorBody()?.toString() ?: "")
        }
    }

    override suspend fun fetchRestaurantPhoto(restaurantId: String): Resource<String> {
        return Resource.success("")
    }
}