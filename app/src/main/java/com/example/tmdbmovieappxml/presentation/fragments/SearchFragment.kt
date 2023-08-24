package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovieappxml.R
import androidx.core.widget.addTextChangedListener
import com.example.tmdbmovieappxml.databinding.FragmentSearchBinding
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.MoviesAdapter
import com.example.tmdbmovieappxml.utils.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        initRecyclerView()
        moviesAdapter.setOnItemClickListener {movieDto->
            showToast(movieDto.original_title)
            goToMovieDetails(movieDto)
        }
        var job: Job? = null
        binding.etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(300)
                if(editable.toString().isNotEmpty()){
                    viewModel.getSearchedMovies(editable.toString())
                }
            }
        }
        viewModel.searchedMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.data?.let { movies ->
                        moviesAdapter.differ.submitList(movies.results)
                    }
                }
                is NetworkResponse.Loading -> {
                    print("Getting connection")
                }

                is NetworkResponse.Error -> {
                    print("There was an error connecting to the server.")
                }
            }
        }
    }
    private fun goToMovieDetails(movieDto: MovieDto?){
        if(movieDto == null){
            moviesAdapter.setOnItemClickListener {
                showToast("Cant open more information on this movie. ")
            }
        }
        else{
            val bundle = Bundle().apply {
                putSerializable("movieDto", movieDto)
            }
            findNavController().navigate(R.id.searchToSingleMovie, bundle)
        }
    }
    private fun initRecyclerView(){
        moviesAdapter = MoviesAdapter()
        binding.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}