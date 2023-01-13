package com.udacity.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import retrofit2.HttpException

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

        return try {
            repository.refreshAsteroids()
            Result.success()

        } catch (e: HttpException) {
            Result.retry()
        }
    }
}