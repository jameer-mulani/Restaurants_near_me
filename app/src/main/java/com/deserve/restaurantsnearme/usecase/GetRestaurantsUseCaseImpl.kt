package com.deserve.restaurantsnearme.usecase

import com.deserve.restaurantsnearme.data.RestaurantRepository
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.deserve.restaurantsnearme.model.usecase.GetRestaurantsUseCase

class GetRestaurantsUseCaseImpl(private val restaurantRepository: RestaurantRepository): GetRestaurantsUseCase {
    override suspend fun invoke(input: GetRestaurantInput): Resource<List<Restaurant>> {
        return restaurantRepository.fetchRestaurant(input)
    }
}