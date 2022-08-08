package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.domain.Asteroid



@Dao
interface AsteroidDao {
    @Query("select * from asteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("select * from asteroid Where closeApproachDate=:startDate")
    fun getTodayAsteroid(startDate:String):LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids:ArrayList<Asteroid>)

    @Query("select * from pictureofday")
    fun getPictureOfDayDb(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDay: PictureOfDay)
}

@Database(entities = [Asteroid::class,PictureOfDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}
private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidDatabase::class.java,
                    "database_asteroids").build()
        }
    }
    return INSTANCE
}