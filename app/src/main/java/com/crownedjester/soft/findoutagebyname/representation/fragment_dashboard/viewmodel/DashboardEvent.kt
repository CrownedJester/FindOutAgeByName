package com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.viewmodel

import com.crownedjester.soft.findoutagebyname.domain.model.PersonData

sealed class DashboardEvent {
    data class OnAddToFavorite(val personData: PersonData) : DashboardEvent()
    data class OnSubmitSearchQuery(val query: String) : DashboardEvent()
}
