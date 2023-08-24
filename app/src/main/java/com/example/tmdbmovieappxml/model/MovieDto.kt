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
    val adult: Boolean,
    var backdrop_path: String?,
    val genre_ids: List<Int>?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    var poster_path: String?,
    val release_date: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double?,
    val vote_count: Int?
) : Serializable
