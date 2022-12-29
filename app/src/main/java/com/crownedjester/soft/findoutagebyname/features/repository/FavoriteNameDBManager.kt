package com.crownedjester.soft.findoutagebyname.features.repository

import com.crownedjester.soft.findoutagebyname.features.data_source.FavoriteNameDao
import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteNameDBManager @Inject constructor(private val dao: FavoriteNameDao) :
    FavoriteNameDBRepository {

    override fun getAllNames(): Flow<FavoriteName> = dao.getAllNames()

    override suspend fun addFavoriteName(name: FavoriteName) =
        dao.addFavoriteName(name)


    override suspend fun deleteFavoriteName(name: FavoriteName) =
        dao.deleteFavoriteName(name)

}