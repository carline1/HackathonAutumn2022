package com.example.hackathonautumn2022.di

import com.example.hackathonautumn2022.features.comments.CommentsViewModel
import com.example.hackathonautumn2022.features.main.MainScreenViewModel
import com.example.hackathonautumn2022.features.marks.MarksViewModel
import com.example.hackathonautumn2022.features.task.TaskViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class])
interface AppComponent {

    val mainScreenViewModel: MainScreenViewModel
    val taskViewModel: TaskViewModel.Factory
    val commentsViewModel: CommentsViewModel.Factory
    val marksViewModel: MarksViewModel.Factory

}