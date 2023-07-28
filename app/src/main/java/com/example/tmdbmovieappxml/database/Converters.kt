package com.example.tmdbmovieappxml.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGenreId(genre: List<Int>) : Int{
        return genre[0]
    }

    @TypeConverter
    fun toGenreId(genre: Int): List<Int> {
        return listOf(genre)
    }
}