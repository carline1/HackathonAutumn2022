package com.example.hackathonautumn2022.features.task.usecases

import androidx.annotation.StringRes
import com.example.hackathonautumn2022.data.models.TaskResponse
import com.example.hackathonautumn2022.features.task.usecases.StatusConverter.getStatusByCode

class PrepareTaskScreenUseCase(
    private val taskResponse: TaskResponse
) {

    fun prepare(): TaskScreenData {

        return TaskScreenData(
            description = taskResponse.description ?: "",
            nameOfTask = taskResponse.name ?: "",
            creatorName = taskResponse.user ?: "",
            dateOfCreation = taskResponse.start ?: "",
            deadline = taskResponse.end ?: "",
            status = getStatusByCode(taskResponse.status),
        )
    }

}

data class TaskScreenData(
    var description: String,
    var nameOfTask: String,
    var creatorName: String,
    var dateOfCreation: String,
    var deadline: String,
    @StringRes var status: Int,
)