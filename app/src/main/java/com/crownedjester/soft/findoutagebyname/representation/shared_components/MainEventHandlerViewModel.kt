package com.crownedjester.soft.findoutagebyname.representation.shared_components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainEventHandlerViewModel @Inject constructor() : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.sendEvent(Dispatchers.IO + Job(), event = event)
        }
    }

}