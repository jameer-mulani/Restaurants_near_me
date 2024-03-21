package com.deserve.restaurantsnearme.data

import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.Restaurant

interface RestaurantRepository {

    suspend fun fetchRestaurant(input: GetRestaurantInput) : Resource<List<Restaurant>>

    suspend fun fetchRestaurantPhoto(restaurantId : String) : Resource<String>

}