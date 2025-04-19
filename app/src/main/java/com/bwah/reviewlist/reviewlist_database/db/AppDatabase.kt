package com.bwah.reviewlist.reviewlist_database.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import android.util.Log
import com.bwah.reviewlist.reviewlist_database.db.Dao.UserDao
import com.bwah.reviewlist.reviewlist_database.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: UserDataBase? = null

        private val TAG: String? = UserDataBase::class.simpleName

        fun get(context: Context): UserDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    UserDataBase::class.java, "user_.db")
                    .fallbackToDestructiveMigration()

                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()// 允许在主线程执行查询（仅限调试，生产环境禁用）
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.e(TAG, "onCreate db_name is=" + db.path)
                        }
                    })
                    .build()


            }
            return instance!!
        }


    }

}
