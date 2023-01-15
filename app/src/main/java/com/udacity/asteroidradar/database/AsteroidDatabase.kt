package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay

@Database(version = 1, entities = [AsteroidEntity::class])
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao : AsteroidDao

    companion object {
        @Volatile
        private lateinit var instance: AsteroidDatabase

        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if(!::instance.isInitialized) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        Constants.DATABASE_FILE_NAME)
                        .build()
                }
            }
            return instance!!
        }
    }
}

@Entity(tableName = Constants.TABLE_NAME)
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun List<AsteroidEntity>.asDomainModel() : List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseModel() : List<AsteroidEntity> {
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
