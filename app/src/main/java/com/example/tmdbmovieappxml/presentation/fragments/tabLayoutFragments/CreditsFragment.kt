package com.example.tmdbmovieappxml.presentation.fragments.tabLayoutFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        initRecyclerView()
        initCastRecyclerView()
        initMembers(movieId)
        viewModel.movieCrew.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.data?.let { castResponse ->
                        castAdapter.differ.submitList(castResponse.cast)
                        creditsAdapter.differ.submitList(castResponse.crew)
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
        binding.btnCast.setOnClickListener{
            if(binding.btnCast.text.toString() == getString(R.string.show_cast_members)){
                binding.btnCast.text = getString(R.string.show_crew_members2)
                binding.creditsRecycler.visibility = View.GONE
                binding.castRecycler.visibility = View.VISIBLE
            }
            else{
                binding.btnCast.text = getString(R.string.show_cast_members)
                binding.creditsRecycler.visibility = View.VISIBLE
                binding.castRecycler.visibility = View.GONE
            }
        }
    }
    private fun reportError(message: String){
        Log.d("Credits Fragment Error", message)
    }
    private fun reportLoading(message: String){
        Log.d("Fragment Credits Loading", message)
    }
    private fun initRecyclerView(){
        creditsAdapter = CreditsAdapter()
        binding.creditsRecycler.apply {
            adapter = creditsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        if(creditsAdapter.differ.currentList.isEmpty()){
            showEmptyState()
        }
    }
    private fun initCastRecyclerView(){
        castAdapter = CastAdapter()
        binding.castRecycler.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        if(castAdapter.differ.currentList.isEmpty()){
            showEmptyState()
        }
    }
    private fun initMembers(movieId: Int){
        viewModel.fetchCredits(movieId)
    }
    private fun showEmptyState(){
        binding.apply {
            if(btnCast.text == getString(R.string.show_cast_members)){
                castRecycler.visibility = View.GONE
                tvNoCredits.visibility = View.VISIBLE
                ivCamera.visibility = View.VISIBLE
                ivElipsaEmpty.visibility = View.VISIBLE
            }
            else{
                castRecycler.visibility = View.GONE
                tvNoCredits.visibility = View.VISIBLE
                ivCamera.visibility = View.VISIBLE
                ivElipsaEmpty.visibility = View.VISIBLE
            }
        }
    }
}