package com.udacity.asteroidradar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//for caching
class Repository(private val database : AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

        fun getWeekAsteroids(startDate: String, endDate: String) = database.asteroidDao.getWeekAsteroids(startDate, endDate)

        fun getTodayAsteroid(todayDate: String) = database.asteroidDao.getTodayAsteroid(todayDate)


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroids(startDate: String, endDate: String, apiKey: String) {
        //use IO dispatcher because of disk IO operation
        withContext(Dispatchers.IO) {
            try{
                val result = Network.retrofitService.getAllAsteroids(startDate, endDate, apiKey)
                val asteroids = Network.getAsteroids()
                database.asteroidDao.insertAsteroids(asteroids.asDatabaseModel())
            } catch(e: Exception){
                Log.i("unsucessful", e.message!!)
            }
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        lateinit var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = Network.getPictureOfDay()
        }
        return pictureOfDay
    }
}