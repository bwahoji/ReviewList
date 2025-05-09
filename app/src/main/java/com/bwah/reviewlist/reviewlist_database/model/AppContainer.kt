package com.bwah.reviewlist.reviewlist_database.model

import android.content.Context

interface AppContainer {
    val usersRepository : UsersRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val usersRepository : UsersRepository by lazy {
        OfflineUsersRepository(InventoryDatabase.getDatabase(context).userDao())
    }
}