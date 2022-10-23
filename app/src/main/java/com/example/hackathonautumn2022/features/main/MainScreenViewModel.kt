package com.example.hackathonautumn2022.features.main

import androidx.lifecycle.viewModelScope
import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel
import com.example.hackathonautumn2022.domain.interactors.MainInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val interactor: MainInteractor
) : BaseViewModel<MainScreenViewModel.State, MainScreenViewModel.Actions>(State()) {

    init {
        getUsers { userId, taskId, user, task ->
            onAction(Actions.OnTaskClicked(userId, taskId, user, task))
        }
    }

    private fun getUsers(onTaskClick: (userId: Int, taskId: Int, user: String, task: String) -> Unit) {
        updateState { isLoading = true }

        viewModelScope.launch {
            try {
                val screenData = interactor.getUsers(onTaskClick)
                updateState {
                    this.screenData = screenData
                    isLoading = false
                }
            } catch (e: Exception) {
                updateState { isLoading = false }
                e.printStackTrace()
            }
        }
    }

    data class State(
        var screenData: List<MainScreenAdapter.TaskViewHolderModel> = emptyList(),
        var isLoading: Boolean = false
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {
        class OnTaskClicked(
            val userId: Int,
            val taskId: Int,
            val user: String,
            val task: String
        ) : Actions()
    }

}