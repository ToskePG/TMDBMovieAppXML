package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentSingleMovieBinding
import com.example.tmdbmovieappxml.presentation.MoviesViewModel

class SingleMovieFragment : Fragment(R.layout.fragment_single_movie) {

    private lateinit var binding: FragmentSingleMovieBinding
    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}