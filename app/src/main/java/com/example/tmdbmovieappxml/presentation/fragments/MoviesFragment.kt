package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentMoviesBinding
import com.example.tmdbmovieappxml.presentation.adapters.MoviesAdapter


class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        moviesAdapter = MoviesAdapter()
        binding.moviesRecycler.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        if(moviesAdapter.differ.currentList.isEmpty()){
            binding.apply {
                moviesRecycler.isVisible = false
                camera.isVisible = true
                ivElipse.isVisible = true
                tvNoMovies.isVisible = true
            }
        }
    }
}