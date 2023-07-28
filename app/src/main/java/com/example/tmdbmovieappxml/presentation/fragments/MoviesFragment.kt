package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentMoviesBinding
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.MoviesAdapter
import com.example.tmdbmovieappxml.utils.NetworkResponse

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MoviesViewModel
    private var scrollListener: ScrollListener? = null // Add this line to store the ScrollListener reference

    interface ScrollListener {
        fun onScrollStarted()
        fun onScrollStopped()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMoviesBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        scrollListener = activity as? ScrollListener
        setUpRecyclerView()
        moviesAdapter.setOnItemClickListener { movieDto ->
            showToast(movieDto.original_title)
            goToMovieDetails(movieDto)
        }
        viewModel.topRatedMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.data?.let { moviesResponse ->
                        moviesAdapter.differ.submitList(moviesResponse.results)
                    }
                }
                is NetworkResponse.Error -> {
                    response.message?.let {
                        // Report an error
                    }
                }
                is NetworkResponse.Loading -> {
                    response.message?.let {
                        // Report an error
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpRecyclerView(){
        moviesAdapter = MoviesAdapter()
        binding.moviesRecycler.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        binding.moviesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> scrollListener?.onScrollStarted()
                    RecyclerView.SCROLL_STATE_IDLE -> scrollListener?.onScrollStopped()
                }
            }
        })
    }

    private fun goToMovieDetails(movieDto: MovieDto){
        val bundle = Bundle().apply {
            putSerializable("movieDto", movieDto)
        }
        findNavController().navigate(R.id.singleMovieFragment, bundle)
    }
}