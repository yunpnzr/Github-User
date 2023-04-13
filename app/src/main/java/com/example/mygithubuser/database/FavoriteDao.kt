package com.example.mygithubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Query("SELECT * from favorite")
    fun getAllFavorite(): LiveData<List<Favorite>>

    @Query("SELECT count(*) FROM favorite WHERE favorite.login= :login")
    fun getFavoriteUserByUsername(login: String): Int//LiveData<Favorite>

    @Query("DELETE FROM favorite WHERE favorite.login= :login")
    fun removeUser(login: String): Int //LiveData<Favorite>
}