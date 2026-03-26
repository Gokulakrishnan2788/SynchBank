package com.architect.banking.core.data.di

import android.content.Context
import androidx.room.Room
import com.architect.banking.core.data.db.AppDatabase
import com.architect.banking.core.data.db.dao.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the Room database and all DAO instances.
 * Installed in [SingletonComponent] so the database is a true application singleton.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val DATABASE_NAME = "architect_banking.db"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME,
    ).build()

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase): SessionDao =
        database.sessionDao()
}
