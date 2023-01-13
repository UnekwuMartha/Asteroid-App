package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidApp
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application : Application) : ViewModel() {
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = Repository(database)
    private val _asteroidsList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList


    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: LiveData<Asteroid?>
        get() = _navigateToDetailFragment

    init {
        viewModelScope.launch {
                refreshAsteroidList()
                getPictureOfDay()
        }
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
            repository.refreshAsteroids()
        }
    }

    private fun getPictureOfDay(){
        viewModelScope.launch {
            _pictureOfTheDay.value = repository.getPictureOfDay()
        }
    }

}