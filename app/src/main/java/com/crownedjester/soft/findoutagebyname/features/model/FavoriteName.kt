package com.crownedjester.soft.findoutagebyname.features.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_name_table")
data class FavoriteName(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
