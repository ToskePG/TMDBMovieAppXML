package com.example.tmdbmovieappxml.repository

import com.example.tmdbmovieappxml.database.MoviesDatabase
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.model.RatingDto
import com.example.tmdbmovieappxml.model.RatingResponse
import com.example.tmdbmovieappxml.remote.RetrofitInstance
import retrofit2.Response

class MovieRepository(val moviesDatabase: MoviesDatabase) {
    suspend fun getTopRatedMovies() = RetrofitInstance.api?.getTopRatedMovies()
    suspend fun searchMovies(query: String) = RetrofitInstance.api!!.searchMovies(query = query)
    suspend fun rateMovie(movieId: Int, rating: Double): Response<RatingResponse> {
        val ratingDto = RatingDto(rating)
        return RetrofitInstance.api?.rateMovie(movieId = movieId, ratingDto = ratingDto) ?: error("API is not initialized.")
    }
    suspend fun fethcCredits(movieId: Int) = RetrofitInstance.api?.getCredits(movieId = movieId)
    suspend fun fetchReviews(movieId: Int) = RetrofitInstance.api?.getReviews(movieId = movieId)
    suspend fun insertMovie(movieDto: MovieDto) = moviesDatabase.getMovieDao().insertMovie(movieDto)
    fun getFavouriteMovies() = moviesDatabase.getMovieDao().getFavouriteMovies()
    suspend fun deleteMovie(movieDto: MovieDto) = moviesDatabase.getMovieDao().deleteMovie(movieDto)
}