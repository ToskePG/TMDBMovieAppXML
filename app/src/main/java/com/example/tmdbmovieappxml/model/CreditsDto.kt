package com.example.tmdbmovieappxml.model

data class CreditsDto(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)