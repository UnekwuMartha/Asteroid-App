package com.udacity.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.AsteroidDatabase
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class Worker
    (appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "Worker"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = Repository(database)

        val startDate = getNextSevenDaysFormattedDates()[0]
        val endDate = getNextSevenDaysFormattedDates()[5]

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val previousDayTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val previousDayDate = dateFormat.format(previousDayTime)

        return try {
            repository.refreshAsteroids(startDate, endDate, Constants.API_KEY)
            Result.success()

        } catch (e: HttpException) {
            Result.retry()
        }
    }
}