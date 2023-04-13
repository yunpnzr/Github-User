package com.example.mygithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuser.databinding.ItemsUserBinding
import com.example.mygithubuser.response.FollowResponseItem

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    var listUserFragment = ArrayList<FollowResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUserFragment[position]
        holder.bind(user)
    }

    override fun getItemCount() = listUserFragment.size

    class ListViewHolder(private val _binding: ItemsUserBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(user: FollowResponseItem) {
            _binding.tvUsername.text = user.login //nampilin username
            Glide.with(itemView.context) //glide
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .into(_binding.imgAvatar)
        }
    }
}