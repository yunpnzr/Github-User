package com.example.mygithubuser.viewmodel

import android.app.Application
import android.app.SearchManager.QUERY
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.database.Favorite
import com.example.mygithubuser.database.FavoriteDao
import com.example.mygithubuser.database.FavoriteDatabase
import com.example.mygithubuser.response.DetailUserResponse
import com.example.mygithubuser.response.FollowResponseItem
import com.example.mygithubuser.response.GithubResponse
import com.example.mygithubuser.response.ItemsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (application: Application) : AndroidViewModel(application) {

    //Dao
    private val _userDao: FavoriteDao?
    private val _userDb: FavoriteDatabase?

    //Show & search user
    private val _takeUserList = MutableLiveData<ArrayList<ItemsItem>>()
    val userList: LiveData<ArrayList<ItemsItem>> = _takeUserList

    //Detail user
    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _follower = MutableLiveData<List<FollowResponseItem>>()
    val follower: LiveData<List<FollowResponseItem>> = _follower

    private val _following = MutableLiveData<List<FollowResponseItem>>()
    val following: LiveData<List<FollowResponseItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
        private const val QUERY = "arif"
    }

    init {
        takeUser()
        _userDb = FavoriteDatabase.getDatabase(application)
        _userDao = _userDb?.favoriteDao()
    }

    //Menampilkan user di main activity
    private fun takeUser(){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUser(QUERY)

        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _takeUserList.value = response.body()?.items as ArrayList<ItemsItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    //Mencari user di main activity
    fun searchUser(query : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUser(query)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _takeUserList.value = response.body()?.items as ArrayList<ItemsItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    //Menampilkan detail user
    fun getDetailUser(username : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse>{
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ){
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    //Menampilkan Follower
    fun getFollowers(username : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _follower.value = response.body() as ArrayList<FollowResponseItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    //Menampilkan Following
    fun getFollowing(username : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _following.value = response.body() as ArrayList<FollowResponseItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    //DAO
    fun addToFavorite(login: String, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(login,avatarUrl)
            _userDao?.insert(user)
        }
    }

    fun checkUser(login: String):Int = _userDao?.getFavoriteUserByUsername(login)!!

    fun removeFromFavorite(login: String){
        CoroutineScope(Dispatchers.IO).launch {
            _userDao?.removeUser(login)
        }
    }

    fun getFavoriteUser(): LiveData<List<Favorite>>? {
        return _userDao?.getAllFavorite()
    }
    //Batas DAO
}