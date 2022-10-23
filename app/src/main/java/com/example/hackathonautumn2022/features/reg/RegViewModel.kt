package com.example.hackathonautumn2022.features.reg

import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel

class RegViewModel : BaseViewModel<RegViewModel.State, RegViewModel.Actions>(State()) {

    fun onSettingsClicked() {
        onAction(Actions.OnSettingsClicked)
    }

    data class State(
        val isLoading: Boolean = false
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {
        object OnSettingsClicked : Actions()
    }

}