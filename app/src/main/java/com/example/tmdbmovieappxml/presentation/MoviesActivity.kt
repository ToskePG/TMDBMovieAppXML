package com.example.tmdbmovieappxml.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
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
import com.google.android.material.bottomnavigation.BottomNavigationView

class MoviesActivity : AppCompatActivity(), MoviesFragment.ScrollListener,
    SingleMovieFragmentVisibilityListener {

    lateinit var viewModel: MoviesViewModel
    private lateinit var binding: ActivityMoviesBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
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
    private val fadeInAnimation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fade_in)
    }

    private val fadeOutAnimation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fade_out)
    }

    override fun onScrollStarted() {
        hideBottomNavigationWithAnimation()
        isScrolling = true
    }

    override fun onScrollStopped() {
        isScrolling = false
        showBottomNavigationWithAnimation()
    }

    private fun hideBottomNavigationWithAnimation() {
        if (bottomNavigationView.isVisible) {
            bottomNavigationView.startAnimation(fadeOutAnimation)
            bottomNavigationView.isVisible = false
        }
    }

    private fun showBottomNavigationWithAnimation() {
        val currentFragment = navController.currentDestination?.id ?: return
        bottomNavigationView.isVisible = currentFragment !in listOf(
            R.id.loginFragment,
            R.id.singleMovieFragment
        )

        if (bottomNavigationView.isVisible) {
            bottomNavigationView.startAnimation(fadeInAnimation)
        }
    }

    override fun onSingleMovieFragmentVisible(isVisible: Boolean) {
        isSingleMovieFragmentVisible = isVisible
        if (!isSingleMovieFragmentVisible && !isScrolling) {
            showBottomNavigationWithAnimation()
        }
    }
}