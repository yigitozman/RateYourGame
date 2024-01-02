package com.example.rateyourgame.dataclasses

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ratings",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["gameId", "userId"], unique = true)]
)
data class Rating(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val rating: Int?,
    val userId: Int?,
    val gameId: Int?
)