package com.example.uasppapbnajwan.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Delete
    suspend fun deleteMovie(movie : MovieEntity)

    @Query("DELETE FROM favorite_movies")
    suspend fun deleteAllMovies()


    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: String)
}