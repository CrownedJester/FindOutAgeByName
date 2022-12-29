package com.crownedjester.soft.findoutagebyname.features.repository

import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName
import kotlinx.coroutines.flow.Flow

interface FavoriteNameDBRepository {

    fun getAllNames(): Flow<FavoriteName>

    suspend fun addFavoriteName(name: FavoriteName)

    suspend fun deleteFavoriteName(name: FavoriteName)

}