package com.example.hackathonautumn2022.features.task

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel
import com.example.hackathonautumn2022.consts.Constants
import com.example.hackathonautumn2022.domain.interactors.MainInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: Int,
    @Assisted("taskId") private val taskId: Int,
    @Assisted("user") private val user: String,
    @Assisted("task") private val task: String,
    private val interactor: MainInteractor,
) : BaseViewModel<TaskViewModel.State, TaskViewModel.Actions>(State()) {

    init {
        updateState { isLoading = true }

        viewModelScope.launch {
            try {
                val dataForScreen = interactor.getTaskById(userId, taskId)
                updateState {
                    this.description = dataForScreen.description
                    this.nameOfTask = dataForScreen.description
                    this.creatorName = dataForScreen.creatorName
                    this.dateOfCreation = dataForScreen.dateOfCreation
                    this.deadline = dataForScreen.deadline
                    this.status = dataForScreen.status
                    isLoading = false
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onMarksButtonClicked() {
        onAction(Actions.OnMarksButtonClicked(userId, taskId, user, task))
    }

    fun onCommentsButton() {
        onAction(Actions.OnCommentsButton(userId, taskId, user, task))
    }

    fun updateTaskStatus(statusId: Int) {
        viewModelScope.launch {
            interactor.updateTaskStatus(userId, taskId, statusId)
        }
    }

    fun checkMarksButton() = Constants.userName == user

    data class State(
        var isLoading: Boolean = false,
        var description: String = "",
        var nameOfTask: String = "",
        var creatorName: String = "",
        var dateOfCreation: String = "",
        var deadline: String = "",
        @StringRes var status: Int? = null,
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {
        class OnMarksButtonClicked(val userId: Int, val taskId: Int, val user: String, val task: String) : Actions()
        class OnCommentsButton(val userId: Int, val taskId: Int, val user: String, val task: String) : Actions()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: Int,
            @Assisted("taskId") taskId: Int,
            @Assisted("user") user: String,
            @Assisted("task") task: String,
        ): TaskViewModel
    }

}