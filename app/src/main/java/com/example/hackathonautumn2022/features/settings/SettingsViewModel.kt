package com.example.hackathonautumn2022.features.settings

import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel

class SettingsViewModel : BaseViewModel<SettingsViewModel.State, SettingsViewModel.Actions>(State()) {

    data class State(
        val isLoading: Boolean = false
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {

    }

}