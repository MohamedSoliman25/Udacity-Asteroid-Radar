package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel (application: Application): AndroidViewModel(application) {


    private  val TAG = "MainViewModel"
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidRepository(database)

    val asteroid:LiveData<List<Asteroid>> =asteroidsRepository.asteroids
    val pictureOfDay:LiveData<PictureOfDay> = asteroidsRepository.pictureOfDay


    private lateinit var startDate:String
    private lateinit var endDate:String


    init {

        validateRefreshAsteroid()
        validatePictureOfDay()
//        getPicturesOfDay()
//        viewModelScope.launch {
//            Log.d(TAG, "testDayImage: ${asteroidsRepository.getPictureOfDay(Constants.API_KEY).url}")
//        }
    }
    val todayAsteroid = asteroidsRepository.getTodayAsteroid(startDate)


    fun validateRefreshAsteroid(){
        getCalendarDate()
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids(startDate,endDate,BuildConfig.API_KEY)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    fun validatePictureOfDay(){
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshPictureOFDay(BuildConfig.API_KEY)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

//    fun getPicturesOfDay() = viewModelScope.launch {
//        _pictureOfDay.value = asteroidsRepository.getPictureOfDay(Constants.API_KEY)
//    }


    private fun getCalendarDate() {

        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        startDate = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val lastTime = calendar.time
        endDate = dateFormat.format(lastTime)
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}