package com.example.tmdbmovieappxml.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.database.MoviesDatabase
import com.example.tmdbmovieappxml.databinding.ActivityMoviesBinding
import com.example.tmdbmovieappxml.domain.SingleMovieFragmentVisibilityListener
import com.example.tmdbmovieappxml.presentation.fragments.MoviesFragment
import com.example.tmdbmovieappxml.repository.MovieRepository
import com.example.tmdbmovieappxml.utils.Constants.Companion.showNavigationBarDuration
import com.google.android.material.bottomnavigation.BottomNavigationView

class MoviesActivity : AppCompatActivity(), MoviesFragment.ScrollListener,
    SingleMovieFragmentVisibilityListener {

    lateinit var viewModel: MoviesViewModel
    private lateinit var binding: ActivityMoviesBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private val handler = Handler(Looper.getMainLooper())
    private var isScrolling = false
    private var isSingleMovieFragmentVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        initViewModel()
    }
    private fun initViewModel(){
        val moviesRepository = MovieRepository(MoviesDatabase(this))
        val viewModelProviderFactory = MoviesViewModelProviderFactory(moviesRepository = moviesRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MoviesViewModel::class.java]
    }
    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentShows) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = binding.bottomNavBar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavBar.isVisible = destination.id !in listOf(
                R.id.loginFragment,
                R.id.singleMovieFragment
            )
        }
        bottomNavigationView.setupWithNavController(navController)
    }
    override fun onSingleMovieFragmentVisible(isVisible: Boolean) {
        isSingleMovieFragmentVisible = isVisible
        if (!isSingleMovieFragmentVisible && !isScrolling) {
            showBottomNavigation()
        }
    }

    override fun onScrollStarted() {
        hideBottomNavigation()
        isScrolling = true
    }

    override fun onScrollStopped() {
        isScrolling = false
        showBottomNavigationWithDelay()
    }

    private fun hideBottomNavigation() {
        if (bottomNavigationView.isVisible) {
            bottomNavigationView.animate().translationY(bottomNavigationView.height.toFloat())
            bottomNavigationView.isVisible = false
        }
    }

    private fun showBottomNavigation() {
        val currentFragment = navController.currentDestination?.id ?: return
        bottomNavigationView.isVisible = currentFragment !in listOf(
            R.id.loginFragment,
            R.id.singleMovieFragment
        )
    }

    private fun showBottomNavigationWithDelay() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            showBottomNavigation()
        }, showNavigationBarDuration) // Delay for 2 seconds before showing the bottom navigation
    }
}