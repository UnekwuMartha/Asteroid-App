package com.udacity.asteroidradar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.Network
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

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroids() {
        //use IO dispatcher because of disk IO operation
        withContext(Dispatchers.IO) {
            val asteroids = Network.getAsteroids()
            database.asteroidDao.insertAsteroids(asteroids.asDatabaseModel())
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