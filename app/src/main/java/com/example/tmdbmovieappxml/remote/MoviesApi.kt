package com.example.tmdbmovieappxml.remote

import com.example.tmdbmovieappxml.model.CreditsDto
import com.example.tmdbmovieappxml.model.MoviesDto
import com.example.tmdbmovieappxml.model.RatingDto
import com.example.tmdbmovieappxml.model.RatingResponse
import com.example.tmdbmovieappxml.utils.Constants.Companion.API_KEY
import com.example.tmdbmovieappxml.utils.Constants.Companion.TOKEN_BEARER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY
    ) : Response<MoviesDto>

    @GET("search/movie")
    suspend fun searchMovies(
        @Header("Authorization") tokenBearer: String = TOKEN_BEARER,
        @Query("query") query: String,
        @Query("include_adult") include_adult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ) : Response<MoviesDto>

    @POST("movie/{movie_id}/rating")
    suspend fun  rateMovie(
        @Header("Authorization") tokenBearer: String = TOKEN_BEARER,
        @Path("movie_id") movieId: Int,
        @Body ratingDto: RatingDto
    ): Response<RatingResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Header("Authorization") tokenBearer: String = TOKEN_BEARER,
        @Path("movie_id") movieId: Int
    ) : Response<CreditsDto>
}