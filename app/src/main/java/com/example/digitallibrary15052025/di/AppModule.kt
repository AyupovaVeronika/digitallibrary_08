package com.logan.digitallibrary.di

import android.content.Context
import com.logan.digitallibrary.data.database.AppDatabase
import com.logan.digitallibrary.data.database.dao.BookDao
import com.logan.digitallibrary.data.repository.BookCollectionRepository
import com.logan.digitallibrary.data.repository.BookRepository
import com.logan.digitallibrary.utils.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        @ApplicationContext context: Context,
        bookDao: BookDao
    ): BookRepository {
        return BookRepository(context, bookDao)
    }

    @Provides
    @Singleton
    fun provideBookCollectionRepository(
        bookDao: BookDao
    ): BookCollectionRepository {
        return BookCollectionRepository(bookDao)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
}
