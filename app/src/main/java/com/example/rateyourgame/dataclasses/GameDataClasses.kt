package com.example.rateyourgame.dataclasses

data class Game(
    val id: Int,
    val name: String,
    val rating: Double,
    val description_raw: String,
    val background_image: String,
    val metacritic: String
)

data class GameResponse(val results: List<Game>)


