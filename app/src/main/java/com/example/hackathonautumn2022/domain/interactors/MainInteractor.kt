package com.example.hackathonautumn2022.domain.interactors

import com.example.hackathonautumn2022.data.api.AppService
import com.example.hackathonautumn2022.data.models.MessasgesResponse
import com.example.hackathonautumn2022.data.models.MessageRequest
import com.example.hackathonautumn2022.data.models.UpdateTaskStatusRequest
import com.example.hackathonautumn2022.features.comments.usecases.PrepareMessagesScreenData
import com.example.hackathonautumn2022.features.common.ChatAdapter
import com.example.hackathonautumn2022.features.main.MainScreenAdapter.*
import com.example.hackathonautumn2022.features.main.usecases.PrepareMainScreemUseCase
import com.example.hackathonautumn2022.features.main.usecases.GetUserWithTasksUseCase
import com.example.hackathonautumn2022.features.task.usecases.PrepareTaskScreenUseCase
import com.example.hackathonautumn2022.features.task.usecases.StatusConverter
import com.example.hackathonautumn2022.features.task.usecases.TaskScreenData
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val service: AppService,
    private val getUserWithTasksUseCase: GetUserWithTasksUseCase
) {

    var lastCommentsData: MutableList<MessasgesResponse> = mutableListOf()
        private set

    var lastMarksData: MutableList<MessasgesResponse> = mutableListOf()
        private set

    suspend fun getUsers(onTaskClick: (userId: Int, taskId: Int, user: String, task: String) -> Unit): List<TaskViewHolderModel> {
        val usersWithTasks = getUserWithTasksUseCase.getData()
        return PrepareMainScreemUseCase(usersWithTasks, onTaskClick).get()
    }

    suspend fun getTaskById(userId: Int, taskId: Int): TaskScreenData {
        val taskResponse = service.getTaskOfUser(userId, taskId)
        return PrepareTaskScreenUseCase(taskResponse).prepare()
    }

    suspend fun updateTaskStatus(userId: Int, taskId: Int, statusId: Int) {
        val statusCode = StatusConverter.getCodeByStatus(statusId)
        service.updateTaskStatus(userId, taskId, UpdateTaskStatusRequest(statusCode))
    }

    suspend fun getCommentsOfTask(userId: Int, taskId: Int): List<ChatAdapter.ChatViewHolderModel> {
        val userName = service.getUserById(userId).user?.username ?: ""
        val comments = service.getCommentsOfTask(userId, taskId)
        lastCommentsData = comments as MutableList<MessasgesResponse>
        return PrepareMessagesScreenData(userName, comments).prepare()
    }

    suspend fun sendMessageToComments(
        userId: Int,
        taskId: Int,
        user: String,
        task: String,
        description: String
    ) {
        service.sendMessageToComments(userId, taskId, MessageRequest(user, task, description))
    }

    suspend fun getMarksOfTask(userId: Int, taskId: Int): List<ChatAdapter.ChatViewHolderModel> {
        val userName = service.getUserById(userId).user?.username ?: ""
        val marks = service.getMarksOfTask(userId, taskId)
        lastMarksData = marks as MutableList<MessasgesResponse>
        return PrepareMessagesScreenData(userName, marks).prepare()
    }

    suspend fun sendMessageToMarks(
        userId: Int,
        taskId: Int,
        user: String,
        task: String,
        description: String
    ) {
        service.sendMessageToMarks(userId, taskId, MessageRequest(user, task, description))
    }

}