package com.example.mygithubuser.activity

import android.app.SearchManager
import androidx.appcompat.widget.SearchView
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.viewmodel.MainViewModel
import com.example.mygithubuser.R
import com.example.mygithubuser.preferences.SettingPreferences
import com.example.mygithubuser.viewmodel.ViewModelFactory
import com.example.mygithubuser.adapter.UserAdapter
import com.example.mygithubuser.databinding.ActivityMainBinding
import com.example.mygithubuser.response.ItemsItem
import com.example.mygithubuser.viewmodel.SettingViewModel

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title="My Github User"

        val layoutManager = LinearLayoutManager(this)
        binding.tvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.tvUser.addItemDecoration(itemDecoration)

        mainViewModel.userList.observe(this, { showUser ->
            setReviewData(showUser)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    private fun setReviewData(showUser: ArrayList<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.listUser = showUser

        binding.tvUser.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favorite ->{
                val moveIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveIntent)
                return true
            }
            R.id.setting ->{
                val moveIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(moveIntent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}