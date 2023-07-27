package com.example.tmdbmovieappxml.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbmovieappxml.R
import com.example.tmdbmovieappxml.databinding.SingleCastMemberBinding
import com.example.tmdbmovieappxml.model.Cast
import com.example.tmdbmovieappxml.utils.Constants

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>(){

    inner class CastViewHolder(val binding: SingleCastMemberBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Cast>(){
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            SingleCastMemberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val person = differ.currentList[position]
        holder.binding.apply {
            if(person.profile_path == ""){
                ivProfilePic.setImageResource(R.drawable.cast_picture)
                tvPosition.text = person.known_for_department
                tvFullName.text = person.name
            }
            else {
                Glide.with(ivProfilePic.context).load(Constants.IMAGE_URL + person.profile_path)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(ivProfilePic)
                tvPosition.text = person.known_for_department
                tvFullName.text = person.name
            }
        }
    }
}