package com.deserve.restaurantsnearme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.deserve.restaurantsnearme.data.remote.ApiSpec
import com.deserve.restaurantsnearme.data.remote.RemoteApiRestaurantRepository
import com.deserve.restaurantsnearme.databinding.ActivityMainBinding
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.usecase.GetRestaurantPhotosUseCaseImpl
import com.deserve.restaurantsnearme.usecase.GetRestaurantsUseCaseImpl
import com.deserve.restaurantsnearme.util.showToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val api = ApiSpec.api;
    private val restaurantRepository = RemoteApiRestaurantRepository(api)
    private val getRestaurantsUseCase = GetRestaurantsUseCaseImpl(restaurantRepository)
    private val getRestaurantPhotosUseCase = GetRestaurantPhotosUseCaseImpl(restaurantRepository)
    private val restaurantListAdapter: RestaurantListAdapter by lazy { RestaurantListAdapter() }

    private val restaurantViewModel: RestaurantViewModel by lazy {
        ViewModelProvider(
            this,
            RestaurantViewModel.getFactory(getRestaurantsUseCase, getRestaurantPhotosUseCase)
        )[RestaurantViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            restaurantList.adapter = restaurantListAdapter
            restaurantList.hasFixedSize()
            restaurantList.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        restaurantListAdapter.itemClickListener = {
            //start activity

            val intent = Intent(this, RestaurantDetailViewActivity::class.java).apply {
                putExtra(RestaurantDetailViewActivity.RESTAURANT_ARG, it)
            }
            startActivity(intent)

        }
        observeRestaurantList()
        restaurantViewModel.getRestaurants(GetRestaurantInput())
    }

    private fun observeRestaurantList() {
        restaurantViewModel.restaurantList.observe(this) {
            if (it.errorMessage != null) {
                showToast(it.errorMessage)
            } else {
                restaurantListAdapter.items = it.restaurants
                restaurantListAdapter.notifyDataSetChanged()
            }
        }
    }
}