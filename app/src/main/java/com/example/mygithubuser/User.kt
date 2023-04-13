package com.example.mygithubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var name : String,
    var username : String,
    var avatarUrl : String,
    var company: String,
    var location: String,
    var repository: Int,
    var follower: Int,
    var following: Int
) : Parcelable