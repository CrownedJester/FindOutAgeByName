package com.crownedjester.soft.findoutagebyname.data.model

import com.crownedjester.soft.findoutagebyname.domain.model.PersonData

data class PersonDataDto(
    val age: Int,
    val count: Int,
    val name: String
)

fun PersonDataDto.toPersonData(): PersonData =
    PersonData(
        age = this.age,
        name = this.name
    )

