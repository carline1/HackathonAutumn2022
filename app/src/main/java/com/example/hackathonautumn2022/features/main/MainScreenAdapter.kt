package com.example.hackathonautumn2022.features.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathonautumn2022.data.models.TaskResponse
import com.example.hackathonautumn2022.data.models.UserResponse
import com.example.hackathonautumn2022.databinding.MainScreenTaskViewHolderBinding
import com.example.hackathonautumn2022.databinding.MainScreenTitleViewHolderBinding

class MainScreenAdapter :
    ListAdapter<MainScreenAdapter.TaskViewHolderModel, MainScreenAdapter.ViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (ViewHolderType.values()[viewType]) {
            ViewHolderType.TITLE -> ViewHolder.Title(
                MainScreenTitleViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ViewHolderType.TASK -> ViewHolder.Task(
                MainScreenTaskViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(currentList[position])

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is TaskViewHolderModel.Title -> ViewHolderType.TITLE.ordinal
            is TaskViewHolderModel.Task -> ViewHolderType.TASK.ordinal
        }
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(model: TaskViewHolderModel)

        class Title(private val binding: MainScreenTitleViewHolderBinding) :
            ViewHolder(binding.root) {
            override fun bind(model: TaskViewHolderModel) {
                model as TaskViewHolderModel.Title
                binding.root.text = model.title
            }
        }

        class Task(private val binding: MainScreenTaskViewHolderBinding) :
            ViewHolder(binding.root) {
            override fun bind(model: TaskViewHolderModel) {
                model as TaskViewHolderModel.Task
                binding.apply {
                    taskIndex.text = layoutPosition.toString()
                    taskName.text = model.task.name
                    taskDateCreation.text = model.task.start
                    taskDateOfDeath.text = model.task.end
                }
                binding.root.setOnClickListener {
                    model.onClick(
                        model.userInfo.id ?: 0,
                        model.task.id ?: 0,
                        model.userInfo.user?.username ?: "",
                        model.task.name ?: ""
                    )
                }
            }
        }
    }

    enum class ViewHolderType {
        TITLE, TASK
    }

    sealed class TaskViewHolderModel {
        data class Title(val title: String) : TaskViewHolderModel()
        data class Task(
            val userInfo: UserResponse,
            val task: TaskResponse,
            val onClick: (userId: Int, taskId: Int, user: String, task: String) -> Unit
        ) : TaskViewHolderModel()
    }

    object DiffCallback : DiffUtil.ItemCallback<TaskViewHolderModel>() {
        override fun areItemsTheSame(
            oldItem: TaskViewHolderModel,
            newItem: TaskViewHolderModel
        ) = if (oldItem is TaskViewHolderModel.Title && newItem is TaskViewHolderModel.Title) {
            oldItem.title == newItem.title
        } else if (oldItem is TaskViewHolderModel.Task && newItem is TaskViewHolderModel.Task) {
            oldItem.task.id == newItem.task.id
        } else false

        override fun areContentsTheSame(
            oldItem: TaskViewHolderModel,
            newItem: TaskViewHolderModel
        ) = if (oldItem is TaskViewHolderModel.Title && newItem is TaskViewHolderModel.Title) {
            oldItem == newItem
        } else if (oldItem is TaskViewHolderModel.Task && newItem is TaskViewHolderModel.Task) {
            oldItem.task == newItem.task
        } else false
    }

}