package com.example.tmdbmovieappxml.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.tmdbmovieappxml.domain.SingleMovieFragmentVisibilityListener
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.MoviesAdapter
import com.example.tmdbmovieappxml.utils.NetworkResponse

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MoviesViewModel
    private var scrollListener: ScrollListener? = null
    private var singleMovieFragmentVisibilityListener: SingleMovieFragmentVisibilityListener? = null


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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SingleMovieFragmentVisibilityListener) {
            singleMovieFragmentVisibilityListener = context
        }
    }
    private fun setSingleMovieFragmentVisibility(isVisible: Boolean) {
        singleMovieFragmentVisibilityListener?.onSingleMovieFragmentVisible(isVisible)
    }
    override fun onResume() {
        super.onResume()
        setSingleMovieFragmentVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        setSingleMovieFragmentVisibility(true)
    }
    override fun onDetach() {
        super.onDetach()
        singleMovieFragmentVisibilityListener = null
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
                    response.message?.let { reportMessage->
                        logErrorMessage(reportMessage)
                    }
                }
                is NetworkResponse.Loading -> {
                    response.message?.let { reportMessage->
                        logLoadingMessage(reportMessage)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun logLoadingMessage(message: String){
        Log.d("MoviesFragmentLoading", message)
    }
    private fun logErrorMessage(message: String){
        Log.d("MoviesFragmentError", message)
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