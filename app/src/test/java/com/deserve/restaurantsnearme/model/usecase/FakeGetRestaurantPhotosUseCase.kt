package com.deserve.restaurantsnearme.model.usecase

import com.deserve.restaurantsnearme.data.RestaurantRepository
import com.deserve.restaurantsnearme.model.entity.Resource

class FakeGetRestaurantPhotosUseCase(private val repository: RestaurantRepository) :
    GetRestaurantPhotosUseCase {
    override suspend fun invoke(input: String): Resource<String> {
        return repository.fetchRestaurantPhoto(input)
    }
}