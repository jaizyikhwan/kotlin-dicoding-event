package com.example.fundamentalsatu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fundamentalsatu.data.model.FavoriteEvent

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(event: FavoriteEvent)

    @Delete
    suspend fun removeFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    suspend fun getEventById(id: String): FavoriteEvent?
}