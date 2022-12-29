package com.crownedjester.soft.findoutagebyname.features.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteNameDao {

    @Query("select * from favorite_name_table")
    fun getAllNames(): Flow<FavoriteName>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteName(name: FavoriteName)

    @Delete
    suspend fun deleteFavoriteName(name: FavoriteName)

}