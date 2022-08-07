package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*


class RefreshDataWorker(context: Context, params: WorkerParameters):

    CoroutineWorker(context, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val asteroidDatabase = getDatabase(applicationContext)
        val asteroidRepository = AsteroidRepository(asteroidDatabase)

        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val startDate = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val lastTime = calendar.time
        val endDate = dateFormat.format(lastTime)

        return try {
            asteroidRepository.refreshAsteroids(startDate, endDate, BuildConfig.API_KEY)
            asteroidRepository.refreshPictureOFDay(BuildConfig.API_KEY)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }

    }
}