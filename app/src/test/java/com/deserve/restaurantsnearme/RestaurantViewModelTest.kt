package com.deserve.restaurantsnearme

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deserve.restaurantsnearme.data.FakeGetRestaurantInput
import com.deserve.restaurantsnearme.data.FakeRestaurantRepository
import com.deserve.restaurantsnearme.model.usecase.FakeGetRestaurantPhotosUseCase
import com.deserve.restaurantsnearme.model.usecase.FakeGetRestaurantsUseCase
import com.jameermulani.hellounittestingandroid.MainCoroutineRule
import com.jameermulani.hellounittestingandroid.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val testCoroutineScope = TestScope(mainCoroutineRule.dispatcher)


    @Test
    fun `calling getRestaurants turns screen state to loading initially`() {
        //Arrange
        val fakeRepository = FakeRestaurantRepository(isSuccessful = true)
        val restaurantViewModel = RestaurantViewModel(
            getRestaurantsUseCase = FakeGetRestaurantsUseCase(fakeRepository),
            getRestaurantPhotoUseCase = FakeGetRestaurantPhotosUseCase(fakeRepository)
        )

        //Act
        restaurantViewModel.getRestaurants(restaurantInput = FakeGetRestaurantInput)

        //Assert
        val expectedLoadingState = RestaurantListScreenUiState(
            isLoading = true,
            errorMessage = null,
            restaurants = emptyList()
        )
        Assert.assertEquals(restaurantViewModel.restaurantList.value, expectedLoadingState)
    }

    @Test
    fun `calling getRestaurants initially empty list returned`() {

        testCoroutineScope.runTest {
            //Arrange
            val fakeRepository = FakeRestaurantRepository(isSuccessful = false)
            val restaurantViewModel = RestaurantViewModel(
                getRestaurantsUseCase = FakeGetRestaurantsUseCase(fakeRepository),
                getRestaurantPhotoUseCase = FakeGetRestaurantPhotosUseCase(fakeRepository)
            )
            //Act
            restaurantViewModel.getRestaurants(restaurantInput = FakeGetRestaurantInput)

            println(restaurantViewModel.restaurantList.getOrAwaitValueTest().restaurants)

            //Assert
            Assert.assertTrue(
                restaurantViewModel.restaurantList.getOrAwaitValueTest().restaurants.isEmpty()
            )
        }

    }

}