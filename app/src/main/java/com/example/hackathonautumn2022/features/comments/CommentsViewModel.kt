package com.example.hackathonautumn2022.features.comments

import androidx.lifecycle.viewModelScope
import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel
import com.example.hackathonautumn2022.consts.Constants
import com.example.hackathonautumn2022.data.models.MessasgesResponse
import com.example.hackathonautumn2022.domain.interactors.MainInteractor
import com.example.hackathonautumn2022.features.comments.usecases.PrepareMessagesScreenData
import com.example.hackathonautumn2022.features.common.ChatAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommentsViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: Int,
    @Assisted("taskId") private val taskId: Int,
    @Assisted("userId") private val user: String,
    @Assisted("taskId") private val task: String,
    private val interactor: MainInteractor
) : BaseViewModel<CommentsViewModel.State, CommentsViewModel.Actions>(State()) {

    init {
        updateState { isLoading = true }

        viewModelScope.launch {
            try {
                while (true) {
                    val screenData = interactor.getCommentsOfTask(userId, taskId)
                    updateState {
                        this.screenData = screenData
                        isLoading = false
                    }
                    delay(2000)
                }
            } catch (e: Exception) {
                updateState { isLoading = false }
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                interactor.lastCommentsData.add(
                    MessasgesResponse(
                        null,
                        Constants.userName,
                        task,
                        message
                    )
                )
                val newScreenData = PrepareMessagesScreenData(interactor.lastCommentsData).prepare()
                updateState { this.screenData = newScreenData }
                onAction(Actions.ScrollListToBottom)
                interactor.sendMessageToComments(userId, taskId, user, task, message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    data class State(
        var screenData: List<ChatAdapter.ChatViewHolderModel> = emptyList(),
        var isLoading: Boolean = false
    ) : BaseState {
        override fun clone() = this.copy()
    }

    sealed class Actions {
        object ScrollListToBottom : Actions()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: Int,
            @Assisted("taskId") taskId: Int,
            @Assisted("userId") user: String,
            @Assisted("taskId") task: String,
        ): CommentsViewModel
    }

}