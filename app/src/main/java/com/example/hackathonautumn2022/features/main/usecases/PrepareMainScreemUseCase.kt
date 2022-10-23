package com.example.hackathonautumn2022.features.main.usecases

import com.example.hackathonautumn2022.features.main.MainScreenAdapter

class PrepareMainScreemUseCase(
    private val usersWithTasks: List<UserWithTasks>,
    private val onTaskClick: (userId: Int, taskId: Int, user: String, task: String) -> Unit
) {

    fun get(): List<MainScreenAdapter.TaskViewHolderModel> {
        val screenData = mutableListOf<MainScreenAdapter.TaskViewHolderModel>()

        usersWithTasks.forEach { user ->
            val userData = user.user
            screenData.add(
                MainScreenAdapter.TaskViewHolderModel.Title(
                    userData.user?.username ?: ""
                )
            )
            screenData.addAll(user.tasks.map {
                MainScreenAdapter.TaskViewHolderModel.Task(
                    userData,
                    it,
                    onTaskClick
                )
            })
        }

        return screenData
    }

}