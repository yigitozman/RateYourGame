package com.example.rateyourgame.services

import com.example.rateyourgame.dataclasses.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): Response<Game>

    @GET("games")
    suspend fun getGames(@Query("key") apiKey: String): Response<GameResponse>
}