package com.example.rateyourgame.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rateyourgame.dataclasses.Rating
import com.example.rateyourgame.dataclasses.User

@Database(entities = [User::class, Rating::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ratingDao(): RatingDao
}