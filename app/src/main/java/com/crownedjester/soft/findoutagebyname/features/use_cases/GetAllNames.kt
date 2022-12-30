package com.crownedjester.soft.findoutagebyname.features.use_cases

import com.crownedjester.soft.findoutagebyname.features.repository.FavoriteNameDBRepository
import javax.inject.Inject

class GetAllNames @Inject constructor(private val repository: FavoriteNameDBRepository) {

    operator fun invoke() = repository.getAllNames()
}