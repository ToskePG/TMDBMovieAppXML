package com.example.tmdbmovieappxml.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.CreditsFragment
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.DescriptionFragment
import com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments.ReviewsFragment

class CustomFragmentPagerAdapter(activity: MoviesActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3 // Number of custom fragments (Description, Credits, Reviews)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DescriptionFragment()
            1 -> CreditsFragment()
            2 -> ReviewsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}