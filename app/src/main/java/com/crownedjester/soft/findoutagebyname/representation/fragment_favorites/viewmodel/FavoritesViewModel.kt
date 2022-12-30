package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName
import com.crownedjester.soft.findoutagebyname.features.use_cases.DeleteName
import com.crownedjester.soft.findoutagebyname.features.use_cases.GetAllNames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getAllNamesUseCase: GetAllNames,
    private val deleteNameUseCase: DeleteName
) : ViewModel() {

    val favoriteNamesLiveData = getAllNamesUseCase().asLiveData()


    init {

    }

    fun deleteName(name: FavoriteName) {
        viewModelScope.launch(Dispatchers.IO + Job()) {
            deleteNameUseCase(name)
        }
    }

}