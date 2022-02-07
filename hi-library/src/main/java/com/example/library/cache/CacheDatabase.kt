package com.example.library.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.library.utils.AppGlobals

@Database(entities = [Cache::class], version = 1,exportSchema = false)
abstract class CacheDatabase : RoomDatabase() {

    abstract val cacheDao: CacheDao

      companion object{
          private var database:CacheDatabase
          init {
            val context = AppGlobals.get()!!.applicationContext
              database = Room.databaseBuilder(context,CacheDatabase::class.java,"cache").build()
          }

          fun get():CacheDatabase{
              return  database
          }
      }
}