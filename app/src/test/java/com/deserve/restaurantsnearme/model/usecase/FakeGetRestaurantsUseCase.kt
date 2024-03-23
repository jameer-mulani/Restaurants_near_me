package com.deserve.restaurantsnearme.model.usecase

import com.deserve.restaurantsnearme.data.RestaurantRepository
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.Restaurant

class FakeGetRestaurantsUseCase(private val repository: RestaurantRepository) :
    GetRestaurantsUseCase {
    override suspend fun invoke(input: GetRestaurantInput): Resource<List<Restaurant>> {
        return repository.fetchRestaurant(input)
    }
}