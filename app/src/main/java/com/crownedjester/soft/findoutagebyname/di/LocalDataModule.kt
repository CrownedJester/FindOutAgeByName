package com.crownedjester.soft.findoutagebyname.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.crownedjester.soft.findoutagebyname.features.data_source.FavoriteNameDao
import com.crownedjester.soft.findoutagebyname.features.data_source.FavoriteNameDatabase
import com.crownedjester.soft.findoutagebyname.features.repository.FavoriteNameDBManager
import com.crownedjester.soft.findoutagebyname.features.repository.FavoriteNameDBRepository
import com.crownedjester.soft.findoutagebyname.features.use_cases.AddName
import com.crownedjester.soft.findoutagebyname.features.use_cases.DeleteName
import com.crownedjester.soft.findoutagebyname.features.use_cases.GetAllNames
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideFavoriteNamesDatabase(@ApplicationContext context: Context): RoomDatabase =
        Room.databaseBuilder(
            context,
            FavoriteNameDatabase::class.java,
            FavoriteNameDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideFavoriteNamesDao(database: FavoriteNameDatabase) = database.dao

    @Provides
    @Singleton
    fun provideLocalDataRepository(dao: FavoriteNameDao): FavoriteNameDBRepository =
        FavoriteNameDBManager(dao)


    @Provides
    fun provideGetAllUseCase(repository: FavoriteNameDBRepository) =
        GetAllNames(repository)

    @Provides
    fun provideAddNameUseCase(repository: FavoriteNameDBRepository) =
        AddName(repository)

    @Provides
    fun provideDeleteNameUseCase(repository: FavoriteNameDBRepository) =
        DeleteName(repository)
}