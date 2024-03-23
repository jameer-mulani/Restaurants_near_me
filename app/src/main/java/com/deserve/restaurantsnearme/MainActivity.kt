package com.deserve.restaurantsnearme

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.deserve.restaurantsnearme.data.remote.ApiSpec
import com.deserve.restaurantsnearme.data.remote.RemoteApiRestaurantRepository
import com.deserve.restaurantsnearme.databinding.ActivityMainBinding
import com.deserve.restaurantsnearme.model.dto.GetRestaurantInput
import com.deserve.restaurantsnearme.model.dto.UserLocation
import com.deserve.restaurantsnearme.usecase.GetRestaurantPhotosUseCaseImpl
import com.deserve.restaurantsnearme.usecase.GetRestaurantsUseCaseImpl
import com.deserve.restaurantsnearme.util.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

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

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = false
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    permissionGranted = true
                    restaurantViewModel.setLocationPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    getCurrentLocation()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    permissionGranted = true
                    restaurantViewModel.setLocationPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                    getCurrentLocation()
                }
                else -> {}
            }

            if (!permissionGranted) {
                showToast(getString(R.string.location_perm_denied))
            }
        }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        observeRestaurantList()
        validatePermissionAndGetRestaurants()
    }

    private fun getCurrentLocation() {
        restaurantViewModel.getCurrentLocation(fusedLocationClient, { location ->
            restaurantViewModel.getRestaurants(GetRestaurantInput(UserLocation(location.latitude, location.longitude)))
        }, { throwable ->
            showToast(
                throwable.message ?: getString(R.string.failed_to_get_location)
            )
        })
    }

    private fun validatePermissionAndGetRestaurants() {
        if (!hasAppLocationPermissionGranted()) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            //request users current location here
            getCurrentLocation()
        }
    }

    private fun hasAppLocationPermissionGranted() = PermissionChecker.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PermissionChecker.PERMISSION_GRANTED ||
            PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PermissionChecker.PERMISSION_GRANTED


    private fun observeRestaurantList() {
        restaurantViewModel.restaurantList.observe(this) {
            when(it.isLoading){
                true ->{
                    binding.apply {
                        isLoading = true
                        emptyView.visibility = View.GONE
                        restaurantList.visibility = View.GONE
                    }
                }
                false->{
                    binding.isLoading = false
                    if (it.errorMessage != null) {
                        binding.isEmptyList = true
                        showToast(it.errorMessage)
                    } else {
                        binding.isEmptyList = it.restaurants.isEmpty()
                        restaurantListAdapter.items = it.restaurants
                        restaurantListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}