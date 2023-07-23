package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentSingleMovieBinding
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.adapters.CustomFragmentPagerAdapter
import com.example.tmdbmovieappxml.utils.Constants.Companion.IMAGE_URL
import com.google.android.material.tabs.TabLayoutMediator


class SingleMovieFragment : Fragment(R.layout.fragment_single_movie) {

    private lateinit var binding: FragmentSingleMovieBinding
    private val args: MoviesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleMovieBinding.inflate(layoutInflater,
            container,
            false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = args.movieDto
        binding.apply {
            if(movie!=null){
                Glide.with(ivPoster.context).load(IMAGE_URL + movie.backdrop_path).into(ivPoster)
                tvMovieTitle.text = movie.title
            }
        }
        val pagerAdapter = CustomFragmentPagerAdapter(activity as MoviesActivity, movie!!)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Description"
                1 -> tab.text = "Credits"
                2 -> tab.text = "Reviews"
            }
        }.attach()
    }

}