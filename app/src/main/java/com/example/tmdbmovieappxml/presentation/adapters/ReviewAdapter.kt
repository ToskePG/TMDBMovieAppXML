package com.example.tmdbmovieappxml.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovieappxml.databinding.ItemReviewBinding
import com.example.tmdbmovieappxml.model.Result
import com.example.tmdbmovieappxml.utils.Constants.Companion.IMAGE_URL

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Result>(){
        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val comment = differ.currentList[position]
        holder.binding.apply {
            Glide.with(profilePic.context)
                .load(IMAGE_URL + comment.author_details.avatar_path)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(profilePic)
            username.text = comment.author
            rating.text = comment.author_details.rating.toString()
        }
    }


}