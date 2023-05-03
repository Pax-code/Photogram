package com.example.instagramclonekt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclonekt.databinding.RecyclerRowBinding
import com.example.instagramclonekt.models.PostModel

class FeedRecyclerAdapter(private val postList: ArrayList<PostModel>): RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.feedUserNameText.text = postList[position].username
        holder.binding.feedLocationText.text = "📍${postList[position].location}"
        holder.binding.feedCommentText.text = "${postList[position].username}:  ${postList[position].comment}"
        Glide.with(holder.itemView.context).load(postList[position].downloadUrl).into(holder.binding.feedImageView)
    }

}