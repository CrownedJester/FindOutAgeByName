package com.crownedjester.soft.findoutagebyname.features.use_cases

import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName
import com.crownedjester.soft.findoutagebyname.features.repository.FavoriteNameDBRepository
import javax.inject.Inject

class DeleteName @Inject constructor(private val repository: FavoriteNameDBRepository) {

    suspend operator fun invoke(name: FavoriteName) = repository.deleteFavoriteName(name)
}