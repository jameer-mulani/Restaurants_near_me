package com.deserve.restaurantsnearme.model.usecase

import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.Restaurant

interface SuspendUseCase<Input, Output> {
    suspend operator fun invoke(input: Input): Output
}

interface GetRestaurantsUseCase : SuspendUseCase<GetRestaurantInput, Resource<List<Restaurant>>>

interface GetRestaurantPhotosUseCase : SuspendUseCase<String, Resource<String>>

