package com.example.hackathonautumn2022.features.marks

import androidx.lifecycle.viewModelScope
import com.example.core.ui.BaseState
import com.example.core.ui.BaseViewModel
import com.example.hackathonautumn2022.data.models.MessasgesResponse
import com.example.hackathonautumn2022.domain.interactors.MainInteractor
import com.example.hackathonautumn2022.features.comments.CommentsViewModel
import com.example.hackathonautumn2022.features.comments.usecases.PrepareMessagesScreenData
import com.example.hackathonautumn2022.features.common.ChatAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MarksViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: Int,
    @Assisted("taskId") private val taskId: Int,
    @Assisted("userId") private val user: String,
    @Assisted("taskId") private val task: String,
    private val interactor: MainInteractor
) : BaseViewModel<MarksViewModel.State, MarksViewModel.Actions>(State()) {

    init {
        updateState { isLoading = true }

        viewModelScope.launch {
            try {
                val screenData = interactor.getMarksOfTask(userId, taskId)
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

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                interactor.lastMarksData.add(MessasgesResponse(null, user, task, message))
                val newScreenData = PrepareMessagesScreenData(
                    user,
                    interactor.lastMarksData
                ).prepare()
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
        ): MarksViewModel
    }

}