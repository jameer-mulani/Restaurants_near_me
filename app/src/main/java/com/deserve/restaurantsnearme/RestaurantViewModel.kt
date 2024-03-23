package com.deserve.restaurantsnearme

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.deserve.restaurantsnearme.location.LocationUtility
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.entity.Resource
import com.deserve.restaurantsnearme.model.entity.ResourceStatus
import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.deserve.restaurantsnearme.model.usecase.GetRestaurantPhotosUseCase
import com.deserve.restaurantsnearme.model.usecase.GetRestaurantsUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getRestaurantPhotoUseCase: GetRestaurantPhotosUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "RestaurantViewModel"
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

        _restaurantList.value = RestaurantListScreenUiState(isLoading = true, errorMessage = null)

        viewModelScope.launch(coroutineExceptionHandler) {
            val result = getRestaurantsInternal(restaurantInput)
            when (result.status) {
                ResourceStatus.SUCCESS -> {
                    _restaurantList.value =
                        RestaurantListScreenUiState(
                            restaurants = getRestaurantsWithPhotos(
                                result.data ?: emptyList(),
                            ),
                            isLoading = false,
                            errorMessage = null
                        )
                }

                ResourceStatus.LOADING -> {
                    _restaurantList.value = RestaurantListScreenUiState(isLoading = true)
                }

                ResourceStatus.FAILED -> _restaurantList.value =
                    RestaurantListScreenUiState(errorMessage = result.error)

                else -> {
                    //no-ops
                }
            }
        }

    }

    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onSuccess: (Location) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        _restaurantList.value = RestaurantListScreenUiState(isLoading = true, errorMessage = null)
        LocationUtility.getCurrentLocation(fusedLocationProviderClient, onSuccess, onError)
    }

    fun setLocationPriority(locationPriority: Int) {
        LocationUtility.locationPriority = locationPriority
    }

    private suspend fun getRestaurantsWithPhotos(restaurants: List<Restaurant>): List<Restaurant> {
        return withContext(Dispatchers.IO + coroutineExceptionHandler) {
            return@withContext restaurants.map { restaurant ->
                val response = getRestaurantPhotoUseCase(restaurant.id)
                when (response.status) {
                    ResourceStatus.SUCCESS -> {
                        restaurant.imageUrl = response.data
                    }

                    ResourceStatus.LOADING -> {}
                    ResourceStatus.FAILED -> {}
                    else -> {}
                }
                restaurant
            }
        }
    }

    private suspend fun getRestaurantsInternal(restaurantInput: GetRestaurantInput): Resource<List<Restaurant>> {
        return withContext(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val result = getRestaurantsUseCase(restaurantInput)
                Log.d(TAG, "getRestaurantsInternal: $result")
                return@withContext result
            } catch (e: Exception) {
                Log.e(TAG, "getRestaurantsInternal: Failed to load restaurants", e)
                return@withContext Resource.failed(
                    e.message ?: "failed to load restaurants, try again!"
                )
            }
        }
    }

}

data class RestaurantListScreenUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)