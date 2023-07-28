package com.example.tmdbmovieappxml.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbmovieappxml.model.MovieDto

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieDto: MovieDto): Long

    @Query("SELECT * FROM movies")
    fun getFavouriteMovies(): LiveData<List<MovieDto>>

    @Delete
    suspend fun deleteMovie(movieDto: MovieDto)
}
