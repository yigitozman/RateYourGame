package com.example.rateyourgame.instances

import com.example.rateyourgame.services.RawgApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.rawg.io/api/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val rawgApi: RawgApiService = retrofit.create(RawgApiService::class.java)
}