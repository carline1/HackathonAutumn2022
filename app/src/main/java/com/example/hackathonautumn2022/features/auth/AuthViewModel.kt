package com.example.hackathonautumn2022.features.auth

import androidx.lifecycle.viewModelScope
import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel
import com.example.hackathonautumn2022.domain.interactors.MainInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val interactor: MainInteractor
) : BaseViewModel<AuthViewModel.State, AuthViewModel.Actions>(State()) {

    fun onAuthButtonClicked(login: String, pwd: String) {
        viewModelScope.launch {
            try {
                interactor.login(login, pwd)
                onAction(Actions.ToNextScreen(login))
            } catch (e: Exception) {
                onAction(Actions.OnLoginError)
                e.printStackTrace()
            }
        }
    }

    data class State(
        val isLoading: Boolean = false
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {
        class ToNextScreen(val login: String) : Actions()

        object OnLoginError : Actions()
    }

}