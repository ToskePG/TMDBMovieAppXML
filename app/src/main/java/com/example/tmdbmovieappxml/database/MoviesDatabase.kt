package com.example.tmdbmovieappxml.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tmdbmovieappxml.model.MovieDto

@Database(
    entities = [MovieDto::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun getMovieDao() : MovieDao

    companion object{
        @Volatile
        private var instance : MoviesDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MoviesDatabase::class.java, "movie_db.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}