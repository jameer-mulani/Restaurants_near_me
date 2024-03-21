package com.deserve.restaurantsnearme.data.remote

import com.deserve.restaurantsnearme.BuildConfig
import com.deserve.restaurantsnearme.model.dto.SearchPlacesApiResponse
import com.deserve.restaurantsnearme.model.entity.Restaurant
import com.deserve.restaurantsnearme.util.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ApiSpec {

    @GET("search")
    suspend fun searchPlaces(
        @Header("accept") accept: String = "application/json",
        @Header("Authorization") authorization: String = BuildConfig.FSQ_APIKEY,
        @Query("ll") ll: String = "47.606,-122.349358",
        @Query("open_now") openNow: String = "true",
        @Query("sort") sort: String = "DISTANCE"
    ): Response<SearchPlacesApiResponse>

    companion object {
        val api: ApiSpec = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiSpec::class.java)
    }


}