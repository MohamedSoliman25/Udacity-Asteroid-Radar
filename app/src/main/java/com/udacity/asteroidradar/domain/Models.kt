package com.udacity.asteroidradar.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Asteroid(
        @PrimaryKey
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean) : Parcelable{
            fun potentiallyHazardousDescription():String{
                return "asteroid is ${if (isPotentiallyHazardous) "" else "not"} potentially hazardous"
            }
        }
