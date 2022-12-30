package com.crownedjester.soft.findoutagebyname.features.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName

@Database(
    version = 1,
    entities = [FavoriteName::class],
    exportSchema = false

)
abstract class FavoriteNameDatabase() : RoomDatabase() {

    abstract val dao: FavoriteNameDao

    companion object {
        const val DATABASE_NAME = "favorite_names_database"
    }
}