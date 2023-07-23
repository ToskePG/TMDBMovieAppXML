package com.example.tmdbmovieappxml.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.CreditsFragment
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.DescriptionFragment
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.ReviewsFragment

class CustomFragmentPagerAdapter(activity: MoviesActivity,private val movieDto: MovieDto) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3 // Number of custom fragments (Description, Credits, Reviews)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                // Pass the movieDto to the DescriptionFragment
                val fragment = DescriptionFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable("movieDto", movieDto)
                }
                fragment
            }
            1 -> CreditsFragment()
            2 -> ReviewsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}