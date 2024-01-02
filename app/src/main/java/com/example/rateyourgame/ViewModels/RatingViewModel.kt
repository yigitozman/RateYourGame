package com.example.rateyourgame.ViewModels

import androidx.lifecycle.ViewModel
import com.example.rateyourgame.database.RatingDao
import com.example.rateyourgame.dataclasses.Rating

class RatingViewModel(private val ratingDao: RatingDao) : ViewModel() {

    suspend fun insertOrUpdateRow(userIdValue: Int?, gameIdValue: Int, ratingValue: Int) {
        val rating = Rating(userId = userIdValue, gameId = gameIdValue, rating = ratingValue)
        ratingDao.insertOrUpdateRelatedEntity(rating)
    }

    suspend fun getAverageRatingByGameId(gameId: Int): Float? {
        return ratingDao.getAverageRatingByGameId(gameId)
    }

    suspend fun getRatingScoreByUserIdAndGameId(userId: Int?, gameId: Int): Int? {
        return ratingDao.getRatingScoreByUserIdAndGameId(userId, gameId)
    }
    suspend fun getRatingCount(gameId: Int): Int {
        return ratingDao.getRatingCount(gameId)
    }
}