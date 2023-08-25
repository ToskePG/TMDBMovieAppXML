package com.example.tmdbmovieappxml.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "movies"
)
data class MovieDto(
    @PrimaryKey
    val id: Int,
    var adult: Boolean,
    var backdrop_path: String?,
    var genre_ids: List<Int>?,
    var original_language: String?,
    var original_title: String?,
    var overview: String?,
    var popularity: Double?,
    var poster_path: String?,
    var release_date: String?,
    val title: String,
    var video: Boolean,
    var vote_average: Double?,
    var vote_count: Int?
) : Serializable
