package me.inflowsolutions.muzzexercise.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel
<State : ViewModelState,
    ViewState : UiState,
    ViewEvent : UiEvent,
    > : ViewModel() {
    abstract val viewModelStateFlow: StateFlow<State>
    abstract val uiStateFlow: StateFlow<ViewState>
    abstract val uiEventsFlow: MutableSharedFlow<ViewEvent>
    abstract fun State.toUiState(): ViewState
    abstract fun setEvent(event: ViewEvent)
    abstract fun handleEvents(event: ViewEvent)
}

// TODO: Reducers were not necessary
