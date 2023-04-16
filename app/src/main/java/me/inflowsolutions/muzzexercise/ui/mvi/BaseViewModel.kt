package me.inflowsolutions.muzzexercise.ui.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel
<State : ViewModelState,
    ViewState : UiState,
    ViewEvent : UiEvent,
    > : ViewModel() {
    abstract val viewModelStateFlow: StateFlow<State>
    abstract val uiStateFlow: StateFlow<ViewState>
    abstract val uiEventsFlow: MutableSharedFlow<ViewEvent>
    abstract suspend fun State.toUiState(): ViewState
    abstract fun setEvent(event: ViewEvent)
    abstract fun processEvent(event: ViewEvent)
}

// TODO: Reducers were not necessary
