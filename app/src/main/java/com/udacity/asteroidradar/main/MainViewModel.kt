package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application : Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = Repository(database)
    val asteroids = repository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val startDate = getNextSevenDaysFormattedDates()[0]
    private val endDate = getNextSevenDaysFormattedDates()[5]
    private val weekEnd = getNextSevenDaysFormattedDates()[5]

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment
        get() = _navigateToDetailFragment

    init {
        refreshAsteroidList()
        getPictureOfDay()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun navigatedToDetailFragment() {
        _navigateToDetailFragment.value = null
    }

    @SuppressLint("SupportAnnotationUsage")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun refreshAsteroidList(){
        viewModelScope.launch {
            try {
                repository.refreshAsteroids(startDate, endDate, Constants.API_KEY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPictureOfDay(){
        viewModelScope.launch {
            try{
            _pictureOfDay.value = repository.getPictureOfDay()
        } catch (e: Exception) {
            e.printStackTrace()
            }
        }
    }

    fun getWeekAsteroid(): LiveData<List<Asteroid>> {
        val weekAsteroid = Transformations.map(repository.getWeekAsteroids(startDate, weekEnd)) {
            it.asDomainModel()
        }
        return weekAsteroid
    }

    fun getTodayAsteroids(): LiveData<List<Asteroid>> {
        val todayAsteroid = Transformations.map(repository.getTodayAsteroid(startDate)) {
            it.asDomainModel()
        }
        return todayAsteroid
    }

}