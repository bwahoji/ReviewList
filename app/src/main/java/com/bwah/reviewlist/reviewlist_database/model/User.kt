package com.bwah.reviewlist.reviewlist_database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "sex") val sex: String?,
)

data class UserName(val uid: Int, val name: String?)