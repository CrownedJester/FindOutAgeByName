package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.adapter

import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName

interface NamesAdapterCallback {

    fun onLongClickCallback(adapterAction: () -> Unit)

    fun onNameClickCallback(name: FavoriteName)

}