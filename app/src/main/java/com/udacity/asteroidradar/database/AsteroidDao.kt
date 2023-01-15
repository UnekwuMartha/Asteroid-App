package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Constants

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM ${Constants.TABLE_NAME} ORDER BY date(closeApproachDate)")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(asteroids: List<AsteroidEntity>)

    @Query("SELECT * FROM ${Constants.TABLE_NAME} WHERE closeApproachDate <=:date ORDER BY date(closeApproachDate) ASC ")
    fun getTodayAsteroid(date: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM ${Constants.TABLE_NAME}")
    fun deleteAsteroids()

}
