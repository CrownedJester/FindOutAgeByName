package com.crownedjester.soft.findoutagebyname.domain.model

import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName

data class PersonData(
    val age: Int,
    val name: String
)

fun PersonData.toFavoriteName() =
    FavoriteName(name = this.name)
