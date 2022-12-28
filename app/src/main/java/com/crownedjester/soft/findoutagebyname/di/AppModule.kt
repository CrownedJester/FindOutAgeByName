package com.crownedjester.soft.findoutagebyname.di

import android.content.Context
import com.crownedjester.soft.findoutagebyname.common.Client
import com.crownedjester.soft.findoutagebyname.data.data_source.AgeByNameApi
import com.crownedjester.soft.findoutagebyname.domain.repository.AgeByNameManager
import com.crownedjester.soft.findoutagebyname.domain.repository.AgeByNameRepository
import com.crownedjester.soft.findoutagebyname.domain.use_cases.GetAgeByName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun createRetrofit(@ApplicationContext context: Context): Retrofit =
        Retrofit.Builder()
            .baseUrl(AgeByNameApi.BASE_URL)
            .client(Client.createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun createApiService(retrofit: Retrofit): AgeByNameApi =
        retrofit.create(AgeByNameApi::class.java)

    @Singleton
    @Provides
    fun provideRepository(api: AgeByNameApi): AgeByNameRepository =
        AgeByNameManager(api)


    @Provides
    fun provideUseCase(repository: AgeByNameRepository) =
        GetAgeByName(repository)
}