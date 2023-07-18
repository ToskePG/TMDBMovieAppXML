package com.example.tmdbmovieappxml.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tmdbmovieappxml.databinding.MovieItemBinding
import com.example.tmdbmovieappxml.model.MovieDto
import com.example.tmdbmovieappxml.utils.Constants.Companion.IMAGE_URL

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>(){

    inner class MoviesViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<MovieDto>() {
        override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.binding.apply {
            Glide.with(imageResourceId.context).load(IMAGE_URL + movie.backdrop_path).into(imageResourceId)
            tvMoviesTitle.text = movie.title
            tvMoviesDescription.text = movie.overview
            holder.itemView.setOnClickListener{
                onItemClickListener?.let{it(movie)}
            }
        }
    }

    private var onItemClickListener: ((MovieDto) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieDto) -> Unit) {
        onItemClickListener = listener
    }
}