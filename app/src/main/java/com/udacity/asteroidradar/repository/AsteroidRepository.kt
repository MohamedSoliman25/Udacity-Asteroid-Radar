package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {

    private val TAG = "AsteroidRepository"

    val asteroids:LiveData<List<Asteroid>> = asteroidDatabase.asteroidDao.getAllAsteroids()

    suspend fun refreshAsteroids(startDate: String, endDate: String, apiKey: String) {

        withContext(Dispatchers.IO){
            val asteroids = parseAsteroidsJsonResult(
                    JSONObject(
                            Network.asteroids.getAllAsteroids(startDate,endDate,apiKey)
                    )
            )
//            Log.d(TAG, "refreshAsteroids:  ${asteroids}")
            asteroidDatabase.asteroidDao.insertAll(asteroids)
        }
    }

    val pictureOfDay:LiveData<PictureOfDay> = asteroidDatabase.asteroidDao.getPictureOfDayDb()
    suspend fun refreshPictureOFDay(apiKey: String){
        withContext(Dispatchers.IO){
            asteroidDatabase.asteroidDao.insertPictureOfDay(Network.asteroids.getImageOfDay(apiKey))
        }
    }

//    suspend fun getPictureOfDay(apiKey:String):PictureOfDay {
//       return withContext(Dispatchers.IO) {
//            Network.asteroids.getImageOfDay(apiKey)
//        }
//    }
//    suspend fun getPictureOfDay(apiKey:String) =  Network.asteroids.getImageOfDay(apiKey)


    }