package com.example.uasppapbnajwan.network

import com.example.uasppapbnajwan.model.Movie
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("film")
    fun getAllMovies(): Call<List<Movie>>

    @POST("film")
    fun addMovie(@Body requestBody: RequestBody): Call<Movie>


    @POST("film/{id}")
    fun updateMovie(@Path("id") movieId: String, @Body movie: Movie): Call<Movie>


    @DELETE("film/{id}")
    fun deleteMovie(@Path("id") id: String): Call<Unit>
}