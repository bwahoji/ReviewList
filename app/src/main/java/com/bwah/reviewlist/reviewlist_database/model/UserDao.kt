package com.bwah.reviewlist.reviewlist_database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delate(user: User)

    @Query("SELECT * from users WHERE id = :id")
    fun getUser(id: Int):Flow<User>

    @Query("SELECT * from users ORDER BY name ASC")
    fun getAllItems(): Flow<List<User>>
}