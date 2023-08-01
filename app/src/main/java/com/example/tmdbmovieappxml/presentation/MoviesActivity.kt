package com.example.tmdbmovieappxml.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isInvisible
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
    private var isNavigationBarVisible = true
    private var isScrolling = false
    private var isSingleMovieFragmentVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        initViewModel()
    }
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!isSingleMovieFragmentVisible) {
            if (!isScrolling) {
                resetNavigationBarTimer()
            }
        }
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
    override fun onUserInteraction() {
        resetNavigationBarTimer()
    }
    private fun resetNavigationBarTimer() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ hideNavigationBar() }, 2000)
    }
    private fun hideNavigationBar() {
        if (isNavigationBarVisible) {
            binding.bottomNavBar.animate().translationY(bottomNavigationView.height.toFloat())
            isNavigationBarVisible = false
        }
    }
    override fun onScrollStarted() {
        hideBottomNavigation()
        isScrolling = true
    }

    override fun onScrollStopped() {
        isScrolling = false
        showBottomNavigation()
    }
    private fun hideBottomNavigation() {
        binding.bottomNavBar.isInvisible = true
    }
    private fun showBottomNavigation() {
        val currentFragment = navController.currentDestination?.id ?: return
        binding.bottomNavBar.isVisible = currentFragment !in listOf(
            R.id.loginFragment,
            R.id.singleMovieFragment
        )
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ hideBottomNavigation() }, showNavigationBarDuration)
    }
    override fun onSingleMovieFragmentVisible(isVisible: Boolean) {
        isSingleMovieFragmentVisible = isVisible
        if (!isSingleMovieFragmentVisible && !isScrolling) {
            resetNavigationBarTimer()
        }
    }
}