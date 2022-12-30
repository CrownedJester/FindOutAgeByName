package com.crownedjester.soft.findoutagebyname.domain.repository

import com.crownedjester.soft.findoutagebyname.data.data_source.AgeByNameApi
import com.crownedjester.soft.findoutagebyname.data.model.PersonDataDto
import javax.inject.Inject

class AgeByNameManager @Inject constructor(
    private val api: AgeByNameApi
) : AgeByNameRepository {

    override suspend fun getAgeByName(name: String): PersonDataDto =
        api.getAgeByName(name)

}