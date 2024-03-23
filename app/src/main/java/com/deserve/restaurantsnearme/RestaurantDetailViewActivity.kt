package com.deserve.restaurantsnearme

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.deserve.restaurantsnearme.databinding.ActivityRestauarantDetailViewBinding
import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.deserve.restaurantsnearme.util.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class RestaurantDetailViewActivity : AppCompatActivity() {

    companion object {
        const val RESTAURANT_ARG = "restaurant"
    }

    private lateinit var binding: ActivityRestauarantDetailViewBinding
    private var imageLoadingJob: Job? = null

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
                navigateButton.setOnClickListener {
                    openMapApplication(restaurant)
                }
            }
            if (!restaurant.imageUrl.isNullOrEmpty()) {
                imageLoadingJob = lifecycleScope.launch {
                    val bitmap = loadImage(restaurant.imageUrl!!)
                    binding.image.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        imageLoadingJob?.cancel()
    }

    private fun openMapApplication(restaurant: Restaurant) {
        val latitude = restaurant.geoCodes.restaurantGeoCodeMain.latitude
        val longitude = restaurant.geoCodes.restaurantGeoCodeMain.longitude
        val uri = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            try {
                val genericMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(genericMapIntent)
            } catch (innerEx: ActivityNotFoundException) {
                showToast("Device does not have map application open, please install one.")
            }
        }
    }

    private suspend fun loadImage(url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            return@withContext BitmapFactory.decodeStream(URL(url).openStream())
        }
    }
}