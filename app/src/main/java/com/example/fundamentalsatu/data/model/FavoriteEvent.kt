package com.example.fundamentalsatu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var mediaCover: String? = null,
    var ownerName: String? = null,
    var beginTime: String? = null,
    var quota: Int? = null,
    var description: String? = null,
    var registrants: Int?= null,
    var summary: String? = null,
    var link: String? = null
)

