package com.example.tmdbmovieappxml.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.DescriptionFragment
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.ReviewsFragment

class CustomFragmentPagerAdapter(activity: MoviesActivity,private val movieDto: MovieDto) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3 // Number of custom fragments (Description, Credits, Reviews)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                // Pass the movieDto to the DescriptionFragment
                val openDescriptionFragment = DescriptionFragment()
                openDescriptionFragment.arguments = Bundle().apply {
                    putSerializable("movieDto", movieDto)
                }
                openDescriptionFragment
            }
            1 -> {
                // Pass the movieDto to the CreditsFragment
                val openDescriptionFragment = DescriptionFragment()
                openDescriptionFragment.arguments = Bundle().apply {
                    putSerializable("movieDto", movieDto)
                }
                openDescriptionFragment
            }
            2 -> {
                // Pass the movieDto to the ReviewsFragment
                val openReviewsFragment = ReviewsFragment()
                openReviewsFragment.arguments = Bundle().apply {
                    putSerializable("movieDto", movieDto)
                }
                openReviewsFragment
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}