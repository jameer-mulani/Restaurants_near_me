package com.deserve.restaurantsnearme.data.remote

import com.deserve.restaurantsnearme.BuildConfig
import com.deserve.restaurantsnearme.model.dto.PlacePhotoResponse
import com.deserve.restaurantsnearme.model.dto.SearchPlacesApiResponse
import com.deserve.restaurantsnearme.util.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiSpec {

    @GET("search")
    suspend fun searchPlaces(
        @Header("accept") accept: String = "application/json",
        @Header("Authorization") authorization: String = BuildConfig.FSQ_APIKEY,
//        @Query("open_now") openNow: String = "true",
        @Query("sort") sort: String = "DISTANCE",
        @Query("query") query : String= "restaurant",
        @Query("ll") location: String
    ): Response<SearchPlacesApiResponse>


    @GET("{fsq_id}/photos")
    suspend fun getPhotos(
        @Header("accept") accept: String = "application/json",
        @Header("Authorization") authorization: String = BuildConfig.FSQ_APIKEY,
        @Path("fsq_id") id: String,
        @Query("limit") limit: Int = 1,
        @Query("sort") sort : String = "newest",
        @Query("classifications") classifications: String = "outdoor"
    ) : Response<List<PlacePhotoResponse>>

    companion object {
        val api: ApiSpec = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiSpec::class.java)
    }


}