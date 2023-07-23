package com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentDescriptionBinding
import com.example.tmdbmovieappxml.presentation.fragments.MoviesFragmentArgs

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private lateinit var binding: FragmentDescriptionBinding
    private val args: MoviesFragmentArgs by navArgs()
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
        if(movieDetails!=null){
            binding.apply {
                tvOverview.text = movieDetails.overview
                rbRatingBar.rating = movieDetails.vote_average.toFloat()
            }
        }
    }
}