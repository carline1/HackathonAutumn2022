package com.example.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModel<T : BaseState, V>(initialState: T) : ViewModel() {

    private val _state: MutableStateFlow<T> = MutableStateFlow(initialState)
    val state: Flow<T> = _state

    private val _action: MutableSharedFlow<V> = MutableSharedFlow(replay = 1)
    val action: Flow<V> = _action.onEach { _action.resetReplayCache() }

    @Suppress("UNCHECKED_CAST")
    protected fun updateState(block: T.() -> Unit) {
        _state.update { state ->
            val newState = state.clone()
            (newState as T).apply(block)
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun setState(block: T.() -> Unit) {
        _state.update { state ->
            state.apply(block)
        }
    }

    protected fun getState(): T {
        return _state.value
    }

    protected fun onAction(action: V) {
        viewModelScope.launch(Dispatchers.Main) {
            _action.emit(action)
        }
    }

    interface Factory<T, V : ViewModel> {
        fun create(state: T): V
    }

}