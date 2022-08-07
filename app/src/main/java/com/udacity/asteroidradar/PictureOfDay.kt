package com.udacity.asteroidradar

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
@Entity
data class PictureOfDay(@Json(name = "media_type")
                        val mediaType: String,
                        @Json(name = "title")
                        val title: String,
                        @Json(name = "url")
                        @PrimaryKey
                        val url: String)