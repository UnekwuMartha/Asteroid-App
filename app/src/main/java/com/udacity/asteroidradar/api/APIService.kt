package com.udacity.asteroidradar.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getAllAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey : String): PictureOfDay
}


object Network {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val retrofitService: AsteroidService by lazy { retrofit.create(AsteroidService::class.java)}


    @RequiresApi(Build.VERSION_CODES.N)
    private val startDate = getNextSevenDaysFormattedDates()[0]
    @RequiresApi(Build.VERSION_CODES.N)
    private val endDate = getNextSevenDaysFormattedDates()[5]
    @RequiresApi(Build.VERSION_CODES.N)
    private val weekEnd = getNextSevenDaysFormattedDates()[5]
    @RequiresApi(Build.VERSION_CODES.N)

    suspend fun getAsteroids(): List<Asteroid> {
        val responseStr = retrofitService.getAllAsteroids(startDate, endDate, Constants.API_KEY)
        val responseJsonObject = JSONObject(responseStr)
        return parseAsteroidsJsonResult(responseJsonObject)
    }

    suspend fun getPictureOfDay() = retrofitService.getPictureOfDay(Constants.API_KEY)
}