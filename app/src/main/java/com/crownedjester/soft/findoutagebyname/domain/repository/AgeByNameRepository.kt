package com.crownedjester.soft.findoutagebyname.domain.repository

import com.crownedjester.soft.findoutagebyname.data.model.PersonDataDto

interface AgeByNameRepository {

    suspend fun getAgeByName(name: String): PersonDataDto

}