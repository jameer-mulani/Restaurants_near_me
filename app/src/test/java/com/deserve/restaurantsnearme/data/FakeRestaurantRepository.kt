package com.deserve.restaurantsnearme.data

import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.Restaurant

class FakeRestaurantRepository(var isSuccessful: Boolean) : RestaurantRepository {
    override suspend fun fetchRestaurant(input: GetRestaurantInput): Resource<List<Restaurant>> {
        if (isSuccessful) {
            return Resource.success(listOf(restaurant1, restaurant2))
        } else {
            return Resource.failed("Failed")
        }
    }

    override suspend fun fetchRestaurantPhoto(
        restaurantId: String,
        width: Int,
        height: Int
    ): Resource<String> {
        if (isSuccessful) {
            return Resource.success(restaurant1.imageUrl!!)
        } else {
            return Resource.failed("Failed")
        }
    }
}