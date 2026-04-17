package com.kodex.guide.ui.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kodex.guide.ui.addscreen.data.Book

@Database(entities = [Book::class], version = 1)
@TypeConverters(BookConverter::class)
abstract class MainDb: RoomDatabase()  {

//    abstract fun bookDao(): PostDao

    abstract val postDao: PostDao
}


