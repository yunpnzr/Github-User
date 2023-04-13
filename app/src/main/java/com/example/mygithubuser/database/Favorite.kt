package com.example.mygithubuser.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity (tableName = "favorite")
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    var login: String = "",
    var avatarUrl: String? = null,
):Parcelable
