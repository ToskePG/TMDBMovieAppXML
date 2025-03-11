package com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.FragmentCreditsBinding
import com.example.tmdbmovieappxml.presentation.MoviesActivity
import com.example.tmdbmovieappxml.presentation.MoviesViewModel
import com.example.tmdbmovieappxml.presentation.adapters.CastAdapter
import com.example.tmdbmovieappxml.presentation.adapters.CreditsAdapter
import com.example.tmdbmovieappxml.utils.NetworkResponse

class CreditsFragment : Fragment() {

    private lateinit var binding: FragmentCreditsBinding
    private lateinit var creditsAdapter: CreditsAdapter
    private lateinit var castAdapter: CastAdapter
    private val args: CreditsFragmentArgs by navArgs()
    private lateinit var viewModel: MoviesViewModel
    // State variable to track whether the cast or credits is shown
    private var isCastVisible: Boolean = false // True if cast is visible, false if credits are visible

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCreditsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MoviesActivity).viewModel
        val movieId = args.movieDto!!.id
        // Set initial visibility based on the state variable
        setRecyclerVisibility()
        initCrewRecycler()
        initCastRecyclerView()
        initMembers(movieId)
        viewModel.movieCrew.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.data?.let { castResponse ->
                        castAdapter.differ.submitList(castResponse.cast)
                        creditsAdapter.differ.submitList(castResponse.crew)
                        if(creditsAdapter.differ.currentList.isEmpty() && creditsAdapter.differ.currentList.isEmpty()){
                            showEmptyState()
                        }else{
                            hideEmptyState()
                        }
                    }
                }
                is NetworkResponse.Error -> {
                    response.message?.let { errorMessage->
                        reportError(errorMessage)
                    }
                }
                is NetworkResponse.Loading -> {
                    response.message?.let { loadingMessage ->
                        reportLoading(loadingMessage)
                    }
                }
            }
        }
        // Toggle RecyclerView visibility based on the button click
        binding.btnCast.setOnClickListener {
            isCastVisible = !isCastVisible  // Toggle the state
            setRecyclerVisibility() // Update RecyclerView visibility based on the state
            // Change button text based on the state
            if (isCastVisible) {
                binding.btnCast.text = getString(R.string.show_crew_members2)
            } else {
                binding.btnCast.text = getString(R.string.show_cast_members)
            }
        }
    }
    private fun reportError(message: String){
        Log.d("Credits Fragment Error", message)
    }
    private fun reportLoading(message: String){
        Log.d("Fragment Credits Loading", message)
    }
    private fun setRecyclerVisibility() {
        // Force both RecyclerViews to be gone before applying visibility based on state
        binding.castRecycler.visibility = View.GONE
        binding.crewRecycler.visibility = View.GONE

        if (isCastVisible) {
            binding.castRecycler.visibility = View.VISIBLE
            Log.d("RecyclerView Visibility", "Cast Recycler is visible")
        } else {
            binding.crewRecycler.visibility = View.VISIBLE
            Log.d("RecyclerView Visibility", "Credits Recycler is visible")
        }
    }
    private fun buttonCastState() : Boolean = binding.btnCast.text.toString() == getString(R.string.show_cast_members)
    private fun initCrewRecycler(){
        creditsAdapter = CreditsAdapter()
        binding.crewRecycler.apply {
            adapter = creditsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    private fun initCastRecyclerView(){
        binding.castRecycler.isVisible = false
        castAdapter = CastAdapter()
        binding.castRecycler.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    private fun hideEmptyState(){
        binding.apply {
                crewRecycler.visibility = View.VISIBLE
                tvNoCredits.visibility = View.GONE
                ivCamera.visibility = View.GONE
                ivElipsaEmpty.visibility = View.GONE
        }
    }
    private fun initMembers(movieId: Int){
        viewModel.fetchCredits(movieId)
    }
    private fun showEmptyState(){
        binding.apply {
                castRecycler.visibility = View.GONE
                tvNoCredits.visibility = View.VISIBLE
                ivCamera.visibility = View.VISIBLE
                ivElipsaEmpty.visibility = View.VISIBLE
        }
    }
}