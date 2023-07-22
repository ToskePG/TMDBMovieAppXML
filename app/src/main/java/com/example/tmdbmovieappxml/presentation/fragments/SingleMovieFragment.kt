package com.example.tmdbmovieappxml.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentSingleMovieBinding
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.CustomFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class SingleMovieFragment : Fragment(R.layout.fragment_single_movie) {

    private lateinit var binding: FragmentSingleMovieBinding
    private lateinit var viewModel: MoviesViewModel

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

        val pagerAdapter = CustomFragmentPagerAdapter(activity as MoviesActivity)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Customize tab titles based on the custom fragments
            when (position) {
                0 -> tab.text = "Description"
                1 -> tab.text = "Credits"
                2 -> tab.text = "Reviews"
                // Add more tabs as needed
            }
        }.attach()
    }

}