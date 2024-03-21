package com.deserve.restaurantsnearme

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.deserve.restaurantsnearme.databinding.ActivityRestauarantDetailViewBinding
import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.deserve.restaurantsnearme.util.showToast

class RestaurantDetailViewActivity : AppCompatActivity() {

    companion object{
        const val RESTAURANT_ARG = "restaurant"
    }

    private lateinit var binding: ActivityRestauarantDetailViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestauarantDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurant: Restaurant? = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            intent.extras?.getParcelable(RESTAURANT_ARG)
        } else {
            intent.extras?.getParcelable(RESTAURANT_ARG, Restaurant::class.java)
        }

        if (restaurant == null) {
            showToast("Something went wrong, please try again!")
            finish()
        } else {
            binding.apply {
                this.restaurant = restaurant
            }
        }
    }
}