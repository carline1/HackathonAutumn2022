package com.example.hackathonautumn2022.features.task

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.databinding.SettingsFragmentBinding
import com.example.hackathonautumn2022.databinding.TaskFragmentBinding
import com.example.hackathonautumn2022.di.AppComponent
import com.example.hackathonautumn2022.di.appComponent
import com.example.hackathonautumn2022.features.comments.CommentsFragment
import com.example.hackathonautumn2022.features.main.MainScreenFragment.Companion.USER_NAME
import com.example.hackathonautumn2022.features.marks.MarksFragment
import com.example.hackathonautumn2022.features.reg.RegViewModel
import com.example.hackathonautumn2022.utils.collectOnStart
import com.example.hackathonautumn2022.utils.navigateTo
import kotlinx.coroutines.flow.onEach

class TaskFragment : Fragment() {

    private var _binding: TaskFragmentBinding? = null
    private val binding get() = _binding!!

    private val component: AppComponent by lazy {
        requireContext().appComponent
    }
    private val viewModel: TaskViewModel by lazy {
        val userId = arguments?.getInt(USER_ID)
        checkNotNull(userId) { "userId must be not null!" }
        val taskId = arguments?.getInt(TASK_ID)
        checkNotNull(taskId) { "taskId must be not null!" }
        val user = arguments?.getString(USER)
        checkNotNull(user) { "user must be not null!" }
        val task = arguments?.getString(TASK)
        checkNotNull(task) { "task must be not null!" }

        component.taskViewModel.create(userId, taskId, user, task)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaskFragmentBinding.inflate(inflater, container, false)

        initToolbar()
        initStatusMenu()
        initMarksButton()
        initCommentsButton()

        return binding.root
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_24px)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initStatusMenu() {
        binding.statusTextLayout.setOnClickListener {
            showMenu(it, R.menu.task_status_menu)
        }
    }

    private fun initMarksButton() {
        binding.marksButton.setOnClickListener {
            viewModel.onMarksButtonClicked()
        }
    }

    private fun initCommentsButton() {
        binding.commentsButton.setOnClickListener {
            viewModel.onCommentsButton()
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            val statusId = when (menuItem.itemId) {
                R.id.notStarted -> R.string.not_started
                R.id.inProgress -> R.string.in_progress
                R.id.failed -> R.string.failed
                R.id.finished -> R.string.finished
                else -> R.string.not_started
            }
            binding.statusText.text = getString(statusId)
            viewModel.updateTaskStatus(statusId)
            return@setOnMenuItemClickListener true
        }

        popup.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeActions()
    }

    private fun observeState() {
        viewModel.state.onEach {
            updateDescription(it.description)
            updateNameOfTask(it.nameOfTask)
            updateCreatorName(it.creatorName)
            updateDateOfCreation(it.dateOfCreation)
            updateDeadline(it.dateOfCreation)
            updateStatus(it.status)
            updateProgress(it.isLoading)
        }.collectOnStart(viewLifecycleOwner)
    }

    private fun updateProgress(isLoading: Boolean) {
        binding.progress.root.isVisible = isLoading
    }

    private fun updateDescription(description: String) {
        binding.descText.text = description
    }

    private fun updateNameOfTask(nameOfTask: String) {
        binding.toolbar.title = nameOfTask
    }

    private fun updateCreatorName(creatorName: String) {
        binding.creatorNameText.text = creatorName
    }

    private fun updateDateOfCreation(dateOfCreation: String) {
        binding.dateOfCreationText.text = dateOfCreation
    }

    private fun updateDeadline(deadline: String) {
        binding.deadlineText.text = deadline
    }

    private fun updateStatus(status: Int?) {
        binding.statusText.text = getString(status ?: return)
    }

    private fun observeActions() {
        viewModel.action.onEach {
            when (it) {
                is TaskViewModel.Actions.OnMarksButtonClicked -> {
                    val bundle =
                        MarksFragment.prepareBundle(it.userId, it.taskId, it.user, it.task)
                    navigateTo(R.id.action_taskFragment_to_marksFragment, bundle)
                }
                is TaskViewModel.Actions.OnCommentsButton -> {
                    val bundle =
                        CommentsFragment.prepareBundle(it.userId, it.taskId, it.user, it.task)
                    navigateTo(R.id.action_taskFragment_to_commentsFragment, bundle)
                }
            }
        }.collectOnStart(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_ID = "USER_ID"
        const val TASK_ID = "TASK_ID"
        const val USER = "USER"
        const val TASK = "TASK"

        fun prepareBundle(userId: Int, taskId: Int, user: String, task: String) = Bundle().apply {
            putInt(USER_ID, userId)
            putInt(TASK_ID, taskId)
            putString(USER, user)
            putString(TASK, task)
        }
    }

}