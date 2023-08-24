package com.example.tmdbmovieappxml.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovieappxml.model.CreditsDto
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.model.MoviesDto
import com.example.tmdbmovieappxml.model.ReviewDto
import com.example.tmdbmovieappxml.repository.MovieRepository
import com.example.tmdbmovieappxml.utils.NetworkResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MoviesViewModel(private val moviesRepository: MovieRepository) : ViewModel() {

    val topRatedMovies: MutableLiveData<NetworkResponse<MoviesDto>> = MutableLiveData()
    val movieCrew: MutableLiveData<NetworkResponse<CreditsDto>> = MutableLiveData()
    val reviews: MutableLiveData<NetworkResponse<ReviewDto>> = MutableLiveData()
    val searchedMovies: MutableLiveData<NetworkResponse<MoviesDto>> = MutableLiveData()

    init {
        getTopRatedMovies()
    }
    fun fetchCredits(movieId: Int) = viewModelScope.launch {
        movieCrew.postValue(NetworkResponse.Loading())
        val crewResponse = moviesRepository.fethcCredits(movieId)
        movieCrew.postValue(handleCreditsResponse(crewResponse))
    }
    fun getSearchedMovies(query: String) = viewModelScope.launch {
        searchedMovies.postValue(NetworkResponse.Loading())
        val response = moviesRepository.searchMovies(query)
        searchedMovies.postValue(handleMoviesResponse(response))
    }
    fun fetchReviews(movieId: Int) = viewModelScope.launch {
        reviews.postValue(NetworkResponse.Loading())
        val reviewResponse = moviesRepository.fetchReviews(movieId)
        reviews.postValue(handleFetchReviews(reviewResponse))
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

    private fun handleCreditsResponse(response: Response<CreditsDto>) : NetworkResponse<CreditsDto>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                return NetworkResponse.Success(resultResponse)
            }
        }
        return NetworkResponse.Error(response.message())
    }
    private fun handleFetchReviews(response: Response<ReviewDto>) : NetworkResponse<ReviewDto>{
        if(response.isSuccessful){
            response.body()?.let { reviewResponse->
                return NetworkResponse.Success(reviewResponse)
            }
        }
        return NetworkResponse.Error(response.message())
    }
    fun rateMovie(movieId: Int, rating: Double) = viewModelScope.launch {
        moviesRepository.rateMovie(movieId, rating)
    }
    // Room functions
    fun addMovieToDatabase(movieDto: MovieDto){
        viewModelScope.launch {
            moviesRepository.insertMovie(movieDto)
        }
    }

    fun getFavouriteMovies() : LiveData<List<MovieDto>> {
        return moviesRepository.getFavouriteMovies()
    }

    fun removeMovie(movie: MovieDto?) {
        viewModelScope.launch{
            moviesRepository.deleteMovie(movie!!)
        }
    }

    fun validatePassword(password: String) : Boolean{
        val passwordPattern = Regex("(?=.*[A-Z])(?=.*\\d).{8,}")
        return passwordPattern.matches(password)
    }
    fun validateEmail(email: String) : Boolean {
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailPattern.matches(email)
    }
    fun checkLoginButton(email: String, password: String) = validateEmail(email) && validatePassword(password)
}