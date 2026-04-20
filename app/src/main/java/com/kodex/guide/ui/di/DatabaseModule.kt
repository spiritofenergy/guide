package com.kodex.guide.ui.di

import android.content.Context
import androidx.room.Room
import com.kodex.guide.ui.db.MainDb
import com.kodex.guide.ui.db.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MainDb {
        return Room.databaseBuilder(
            context,
            MainDb::class.java,
            "books"
        ).build()
    }

    @Provides
    @Singleton
    fun providePostDao(database: MainDb): TrackDao {
        return database.trackDao // или database.mainDao(), зависит от вашей реализации
    }
}

