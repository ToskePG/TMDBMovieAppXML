package com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.BottomSheetDialogRateMovieBinding
import com.example.tmdbmovieappxml.databinding.FragmentDescriptionBinding
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.fragments.MoviesFragmentArgs
import com.google.android.material.bottomsheet.BottomSheetDialog

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private lateinit var binding: FragmentDescriptionBinding
    private val args: MoviesFragmentArgs by navArgs()
    private lateinit var viewModel: MoviesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieDetails = args.movieDto
        viewModel = (activity as MoviesActivity).viewModel
        if(movieDetails!=null){
            binding.apply {
                tvOverview.text = movieDetails.overview
                val movieRtg = (movieDetails.vote_average)/2
                val rtg = movieRtg.toFloat()
                rbRatingBar.rating = rtg
                tvRating.text = movieRtg.toString()
            }
        }
        binding.apply {
            btnButtonAddRating.setOnClickListener{
                showRatingBottomSheet()
            }
            btnAddToFavourites.setOnClickListener{
                addToFavourites(movieDetails!!)
            }
        }
    }

    private fun showRatingBottomSheet() {
        val dialog = BottomSheetDialog(this.requireContext())
        val bottomSheetBinding = BottomSheetDialogRateMovieBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        dialog.show()
        bottomSheetBinding.ratingBar.setOnRatingBarChangeListener { _, fl, _ ->
            bottomSheetBinding.btnAddRating.isEnabled = fl >= 1
        }
        bottomSheetBinding.btnAddRating.setOnClickListener{
            showToast()
            dialog.dismiss()
            val rating: Double = bottomSheetBinding.ratingBar.rating.toDouble()
            val movieId = args.movieDto?.id
            viewModel.rateMovie(movieId!!, rating)
        }
    }

    private fun addToFavourites(movieDto: MovieDto){
        viewModel.addMovieToDatabase(movieDto)
        showToastForDb()
    }

    private fun showToast(){
        Toast.makeText(
            context,
            "Thank you for your vote! Your rating has been posted!",
            Toast.LENGTH_SHORT).show()
    }

    private fun showToastForDb(){
        Toast.makeText(
            context,
            "Movie has been added to your favourites! ",
            Toast.LENGTH_SHORT).show()
    }
}