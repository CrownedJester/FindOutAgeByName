package com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard

import com.crownedjester.soft.findoutagebyname.domain.model.PersonData

data class UiState(
    val isLoading: Boolean = false,
    val data: PersonData? = null,
    val message: String = ""
)
