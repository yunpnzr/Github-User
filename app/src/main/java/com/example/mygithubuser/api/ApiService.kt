package com.example.mygithubuser.api

import com.example.mygithubuser.response.DetailUserResponse
import com.example.mygithubuser.response.FollowResponseItem
import com.example.mygithubuser.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")

    //Search user
    @GET("search/users")
    fun getUser(
        @Query("q") query: String
    ): Call<GithubResponse>

    //Detail User
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>
}
