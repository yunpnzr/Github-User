package com.example.mygithubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuser.activity.DetailUserActivity
import com.example.mygithubuser.databinding.ItemsUserBinding
import com.example.mygithubuser.response.ItemsItem

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    var listUser = ArrayList<ItemsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.bind(user)
        holder.itemView.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Kamu memilih username : ${user.login}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_NAME,user.login)
            intent.putExtra(DetailUserActivity.EXTRA_URL,user.avatarUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = listUser.size

    class ListViewHolder(private val _binding: ItemsUserBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(user: ItemsItem) {

            _binding.tvUsername.text = user.login //nampilin username

            Glide.with(itemView.context) //glide
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .into(_binding.imgAvatar)
        }
    }

}