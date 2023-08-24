package com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentReviewsBinding
import com.example.tmdbmovieappxml.model.Result
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.ReviewAdapter
import com.example.tmdbmovieappxml.utils.NetworkResponse


class ReviewsFragment : Fragment(R.layout.fragment_reviews) {

    private lateinit var binding: FragmentReviewsBinding
    private val args: ReviewsFragmentArgs by navArgs()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentReviewsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        val movieId = args.movieDto!!.id
        initReviews(movieId)
        initRecyclerView()
        viewModel.reviews.observe(viewLifecycleOwner){response->
            when(response){
                is NetworkResponse.Success -> {
                    response.data?.let { reviewsResponse->
                        logReviwes(reviewsResponse.results)
                        reviewAdapter.differ.submitList(reviewsResponse.results)
                        logCurrentList(reviewAdapter.differ.currentList)
                        if (reviewsResponse.results.isEmpty()) {
                            showEmptyState()
                        } else {
                            hideEmptyState()
                        }
                    }
                }
                is NetworkResponse.Error -> {
                    response.message?.let{ errorMessage->
                        reportError(errorMessage)
                        showEmptyState()
                    }
                }
                is NetworkResponse.Loading -> {
                    response.message?.let { loadingMessage->
                        reportLoadingState(loadingMessage)
                        showEmptyState()
                    }
                }
            }
        }
    }
    private fun logCurrentList(listOfReviews: List<Result>){
        Log.d("Current Review List: ", listOfReviews.toString())
    }
    private fun logReviwes(reviews: List<Result>){
        Log.d("Review List: ", reviews.toString())
    }
    private fun reportError(message: String){
        Log.d("ReviewFragmentError", message)
    }
    private fun reportLoadingState(message: String){
        Log.d("ReviewFragmentLoading", message)
    }
    private fun initReviews(movieId: Int){
        viewModel.fetchReviews(movieId)
    }
    private fun initRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.rvReviews.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvReviews.addItemDecoration(itemDecoration)
        Log.d("ReviewsFragment123", "ReviewAdapter initialized.")
        if(reviewAdapter.differ.currentList.isEmpty()){
            showEmptyState()
        }else{
            hideEmptyState()
        }
    }
    private fun hideEmptyState(){
        binding.apply {
            rvReviews.visibility = View.VISIBLE
            ivElipsaEmpty.visibility = View.GONE
            ivCamera.visibility = View.GONE
            tvNoReviews.visibility = View.GONE
        }
    }
    private fun showEmptyState(){
        binding.apply {
            rvReviews.visibility = View.GONE
            ivElipsaEmpty.visibility = View.VISIBLE
            ivCamera.visibility = View.VISIBLE
            tvNoReviews.visibility = View.VISIBLE
        }
    }
}