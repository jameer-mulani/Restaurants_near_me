package com.deserve.restaurantsnearme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.ResourceStatus
import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.deserve.restaurantsnearme.model.usecase.GetRestaurantPhotosUseCase
import com.deserve.restaurantsnearme.model.usecase.GetRestaurantsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getRestaurantPhoto: GetRestaurantPhotosUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "RestaurantViewModel"

        const val GetRestaurantUseCase = "RestaurantViewModel.GetRestaurantUseCase"
        const val GetRestaurantPhotoUseCase = "RestaurantViewModel.GetRestaurantPhotoUseCase"

        fun getFactory(
            getRestaurantsUseCase: GetRestaurantsUseCase,
            getRestaurantPhotosUseCase: GetRestaurantPhotosUseCase
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return RestaurantViewModel(
                        getRestaurantsUseCase,
                        getRestaurantPhotosUseCase
                    ) as T
                }
            }
        }
    }

    private val _restaurantList = MutableLiveData<RestaurantListScreenUiState>()
    val restaurantList: LiveData<RestaurantListScreenUiState> = _restaurantList


    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    fun getRestaurants(restaurantInput: GetRestaurantInput) {

        viewModelScope.launch(coroutineExceptionHandler) {
            val result = getRestaurantsInternal(restaurantInput)
            when (result.status) {
                ResourceStatus.SUCCESS -> {
                    _restaurantList.value =
                        RestaurantListScreenUiState(restaurants = result.data ?: emptyList())
                }

                ResourceStatus.LOADING -> {
                    //no-ops
                }

                ResourceStatus.FAILED -> _restaurantList.value =
                    RestaurantListScreenUiState(errorMessage = result.error)

                else -> {
                    //no-ops
                }
            }
        }

    }

    private suspend fun getRestaurantsInternal(restaurantInput: GetRestaurantInput): Resource<List<Restaurant>> {
        return withContext(Dispatchers.IO + coroutineExceptionHandler) {
            val result = getRestaurantsUseCase(restaurantInput)
            Log.d(TAG, "getRestaurantsInternal: $result")
            return@withContext result
        }
    }

}

data class RestaurantListScreenUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val errorMessage: String? = null
)