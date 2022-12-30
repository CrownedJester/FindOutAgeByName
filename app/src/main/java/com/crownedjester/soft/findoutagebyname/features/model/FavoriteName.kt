package com.crownedjester.soft.findoutagebyname.features.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_name_table")
data class FavoriteName(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
) {

    @Ignore
    var isSelected: Boolean = false

}
