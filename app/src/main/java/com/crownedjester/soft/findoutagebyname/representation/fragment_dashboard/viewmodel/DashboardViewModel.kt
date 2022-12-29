package com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crownedjester.soft.findoutagebyname.common.Response
import com.crownedjester.soft.findoutagebyname.domain.model.toFavoriteName
import com.crownedjester.soft.findoutagebyname.domain.use_cases.GetAgeByName
import com.crownedjester.soft.findoutagebyname.features.use_cases.AddName
import com.crownedjester.soft.findoutagebyname.representation.common.bundle.BundlePrefs
import com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val NAME_INITIAL_VALUE = "-1"

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAgeByNameUseCase: GetAgeByName,
    private val addNameUseCase: AddName,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _ageLiveData = MutableLiveData(UiState())
    val ageLiveData: LiveData<UiState> get() = _ageLiveData

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(BundlePrefs.NAME_KEY, NAME_INITIAL_VALUE).let {
                it.collectLatest { name ->
                    if (name != NAME_INITIAL_VALUE && name.isNotEmpty()) {
                        Log.i("ViewModel", name.isEmpty().toString())
                        getAgeByName(name)
                    }
                }
            }
        }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnAddToFavorite -> {
                viewModelScope.launch(Dispatchers.IO + Job()) {
                    addNameUseCase(
                        event.personData.toFavoriteName()
                    )
                }
            }

            is DashboardEvent.OnSubmitSearchQuery -> {
                getAgeByName(event.query)
            }
        }
    }

    private fun getAgeByName(name: String) {
        getAgeByNameUseCase(name).onEach {
            when (it) {
                is Response.IsLoading -> _ageLiveData.postValue(UiState(isLoading = true))
                is Response.Success -> _ageLiveData.postValue(UiState(data = it.data))
                is Response.Error -> _ageLiveData.postValue(UiState(message = it.message))
            }
        }.launchIn(viewModelScope)
    }

}