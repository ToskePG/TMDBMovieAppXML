package com.example.tmdbmovieappxml.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tmdbmovieappxml.database.MoviesDatabase
import com.example.tmdbmovieappxml.databinding.ActivityMoviesBinding
import com.example.tmdbmovieappxml.repository.MovieRepository

class MoviesActivity : AppCompatActivity() {

    lateinit var viewModel: MoviesViewModel
    private lateinit var binding: ActivityMoviesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val moviesRepository = MovieRepository(MoviesDatabase(this))
        val viewModelProviderFactory = MoviesViewModelProviderFactory(moviesRepository = moviesRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MoviesViewModel::class.java]
    }
}