package com.example.hackathonautumn2022.features.auth

import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel

class AuthViewModel : BaseViewModel<AuthViewModel.State, AuthViewModel.Actions>(State()) {

    fun onAuthButtonClicked(login: String, pwd: String) {
        onAction(Actions.OnAuthButtonClicked(login, pwd))
    }

    data class State(
        val isLoading: Boolean = false
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {
        class OnAuthButtonClicked(
            val login: String,
            val pwd: String
        ) : Actions()
    }

}