package com.deserve.restaurantsnearme.usecase

import com.deserve.restaurantsnearme.data.RestaurantRepository
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.usecase.GetRestaurantPhotosUseCase

class GetRestaurantPhotosUseCaseImpl(private val restaurantRepository: RestaurantRepository) :
    GetRestaurantPhotosUseCase {
    override suspend fun invoke(input: String): Resource<String> {
        return restaurantRepository.fetchRestaurantPhoto(input)
    }
}