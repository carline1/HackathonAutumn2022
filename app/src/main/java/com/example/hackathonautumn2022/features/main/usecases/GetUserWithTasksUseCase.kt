package com.example.hackathonautumn2022.features.main.usecases

import com.example.hackathonautumn2022.data.api.AppService
import com.example.hackathonautumn2022.data.models.TaskResponse
import com.example.hackathonautumn2022.data.models.UserResponse
import javax.inject.Inject

class GetUserWithTasksUseCase @Inject constructor(
    private val service: AppService
) {

    suspend fun getData(): List<UserWithTasks> {
        val users = service.getUsers()

        val usersWithTasks = mutableListOf<UserWithTasks>()
        users.forEach { user ->
            usersWithTasks.add(
                UserWithTasks(
                    user,
                    service.getTasksOfUser(user.id ?: return@forEach)
                )
            )
        }

        return usersWithTasks
    }

}

data class UserWithTasks(
    val user: UserResponse,
    val tasks: List<TaskResponse>
)