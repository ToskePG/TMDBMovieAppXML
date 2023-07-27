package com.example.tmdbmovieappxml.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovieappxml.databinding.SinglePersonItemBinding
import com.example.tmdbmovieappxml.model.Person
import com.example.tmdbmovieappxml.utils.Constants.Companion.IMAGE_URL

class CreditsAdapter : RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder>(){

    inner class CreditsViewHolder(val binding: SinglePersonItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Person>(){
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditsViewHolder {
        return CreditsViewHolder(
            SinglePersonItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CreditsViewHolder, position: Int) {
        val person = differ.currentList[position]
        holder.binding.apply {
            Glide.with(ivProfilePic.context).load(IMAGE_URL + person.profilePic)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(ivProfilePic)
        }
    }


}