package com.example.tmdbmovieappxml.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovieappxml.model.MoviesDto
import com.example.tmdbmovieappxml.repository.MovieRepository
import com.example.tmdbmovieappxml.utils.NetworkResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MoviesViewModel(private val moviesRepository: MovieRepository) : ViewModel() {

    val topRatedMovies: MutableLiveData<NetworkResponse<MoviesDto>> = MutableLiveData()

    init {
        getTopRatedMovies()
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        topRatedMovies.postValue(NetworkResponse.Loading())
        val moviesResponse = moviesRepository.getTopRatedMovies()
        topRatedMovies.postValue(handleMoviesResponse(moviesResponse!!))
    }

    private fun handleMoviesResponse(response: Response<MoviesDto>) : NetworkResponse<MoviesDto>{
        if(response.isSuccessful){
            response.body()?.let { resultsResponse->
                return NetworkResponse.Success(resultsResponse)
            }
        }
        return NetworkResponse.Error(response.message())
    }
}