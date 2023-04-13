package com.example.mygithubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.adapter.UserAdapter
import com.example.mygithubuser.database.Favorite
import com.example.mygithubuser.databinding.ActivityFavoriteBinding
import com.example.mygithubuser.response.ItemsItem
import com.example.mygithubuser.viewmodel.MainViewModel

class FavoriteActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding : ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title="Favorite User"

        val layoutManager = LinearLayoutManager(this)
        binding.tvFavorite.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.tvFavorite.addItemDecoration(itemDecoration)

        mainViewModel.getFavoriteUser()?.observe(this){ users: List<Favorite>? ->
            val adapter = UserAdapter()
            if (users != null) {
                val items = arrayListOf<ItemsItem>()
                users.map {
                    val item = ItemsItem(login = it.login, avatarUrl = it.avatarUrl)
                    items.add(item)
                }
                adapter.listUser = items
            }
            binding.tvFavorite.adapter = adapter
        }

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }



    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}