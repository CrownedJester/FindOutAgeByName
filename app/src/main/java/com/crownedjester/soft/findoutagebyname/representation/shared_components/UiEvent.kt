package com.crownedjester.soft.findoutagebyname.representation.shared_components

import android.widget.Toast
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

sealed class UiEvent {
    data class OnNavigate<T>(val routeId: Int, val key: String? = null, val arg: T? = null) :
        UiEvent()

    data class ShowToast(
        val type: ToastType,
        val message: String,
        val duration: Int = Toast.LENGTH_LONG
    ) : UiEvent() {
        enum class ToastType {
            ERROR, INFO, NORMAL, SUCCESS, WARNING
        }
    }

    object OnBack : UiEvent()
}

suspend fun Channel<UiEvent>.sendEvent(coroutineContext: CoroutineContext, event: UiEvent) =
    withContext(coroutineContext) {
        this@sendEvent.send(event)
    }
