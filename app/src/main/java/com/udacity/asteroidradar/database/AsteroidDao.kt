package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids_table ORDER BY date(closeApproachDate)")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(asteroids: List<AsteroidEntity>) : Long

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate <=:date ORDER BY date(closeApproachDate) ASC ")
    fun getTodayAsteroid(date: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroids_table")
    fun deleteAsteroids()

}
