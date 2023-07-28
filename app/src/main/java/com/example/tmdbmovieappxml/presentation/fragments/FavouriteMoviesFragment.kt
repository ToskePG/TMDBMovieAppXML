package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentFavouriteMoviesBinding
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.MoviesAdapter
import com.google.android.material.snackbar.Snackbar

class FavouriteMoviesFragment : Fragment(R.layout.fragment_favourite_movies) {

    private lateinit var binding: FragmentFavouriteMoviesBinding
    private lateinit var viewModel: MoviesViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFavouriteMoviesBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        initRecyclerView()
        viewModel.getFavouriteMovies().observe(viewLifecycleOwner) { movies ->
            moviesAdapter.differ.submitList(movies)
            if(moviesAdapter.differ.currentList.isEmpty()){
                binding.apply {
                    moviesRecycler.visibility = View.GONE
                    ivElipse.visibility = View.VISIBLE
                    camera.visibility = View.VISIBLE
                    tvNoMovies.visibility = View.VISIBLE
                }
            }else{
                binding.apply {
                    moviesRecycler.visibility = View.VISIBLE
                    ivElipse.visibility = View.GONE
                    camera.visibility = View.GONE
                    tvNoMovies.visibility = View.GONE
                }
            }
        }
        moviesAdapter.setOnItemClickListener { movieDto ->
            val bundle = Bundle().apply {
                putSerializable("movieDto", movieDto)
            }
            findNavController().navigate(R.id.favToSingleMovies, bundle)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = moviesAdapter.differ.currentList[position]
                viewModel.removeMovie(movie)
                Snackbar.make(view, "Successfully removed movie from Favourites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.addMovieToDatabase(movie)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.moviesRecycler)
        }
    }
    private fun initRecyclerView() {
        moviesAdapter = MoviesAdapter()
        binding.moviesRecycler.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}