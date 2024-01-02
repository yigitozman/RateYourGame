package com.example.rateyourgame.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rateyourgame.dataclasses.Rating

@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRelatedEntity(rating: Rating)

    @Query("SELECT AVG(rating) FROM ratings WHERE gameId = :gameId")
    suspend fun getAverageRatingByGameId(gameId: Int): Float?

    @Query("SELECT rating FROM ratings WHERE userId = :userId AND gameId = :gameId")
    suspend fun getRatingScoreByUserIdAndGameId(userId: Int?, gameId: Int): Int?

    @Query("SELECT COUNT(*) FROM ratings WHERE gameId = :gameId")
    suspend fun getRatingCount(gameId: Int): Int
}