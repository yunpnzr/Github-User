package com.example.mygithubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubuser.viewmodel.MainViewModel
import com.example.mygithubuser.R
import com.example.mygithubuser.adapter.SectionPageAdapter
import com.example.mygithubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding : ActivityDetailUserBinding

    companion object{
        val EXTRA_NAME = "extra_name"
        val EXTRA_URL = "extra_url"
        var username = String()
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        username = intent.getStringExtra(EXTRA_NAME).toString()
        showDetailUser()

        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val sectionsPagerAdapter = SectionPageAdapter(this)
        sectionsPagerAdapter.username = DetailUserActivity.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = mainViewModel.checkUser(username)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0){
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }
        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                mainViewModel.addToFavorite(username, avatarUrl.toString())
            } else {
                mainViewModel.removeFromFavorite(username)
            }
            binding.toggleFavorite.isChecked = _isChecked
        }
    }

    private fun showDetailUser(){
        mainViewModel.getDetailUser(username)
        mainViewModel.userDetail.observe(this){getDetailUser ->
            Glide.with(this)
                .load(getDetailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.tvDetailUser)
            binding.nameUser.text = getDetailUser.name
            binding.usernameDetail.text = getDetailUser.login
            binding.location.text = getDetailUser.location
            binding.countFollowers.text = "${getDetailUser.followers.toString()} Followers"
            binding.countFollowing.text = "${getDetailUser.following.toString()} Following"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}