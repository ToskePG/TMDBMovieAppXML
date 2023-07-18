package com.example.tmdbmovieappxml.model

data class MoviesDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)
